package moe.pine.est.log.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.log.utils.TimeoutCalculator;
import moe.pine.est.murmur.Murmur3;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class MessageLogRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYYMMdd");
    private static final String ITEMS_KEY_PREFIX = "message:";
    private static final String ITEMS_KEY_FORMAT = "message:{{dt}}";
    private static final String ITEM_KEY_FORMAT = "message:{{dt}}::{{hash}}";
    private static final String DT_KEY = "dt";
    private static final String HASH_KEY = "hash";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Murmur3 murmur3;
    private final Clock clock;
    private final TimeoutCalculator timeoutCalculator;
    private final int retentionDays;
    private final Mustache itemsKeyFormat;
    private final Mustache itemKeyFormat;

    public MessageLogRepository(
        final RedisTemplate<String, String> redisTemplate,
        final ObjectMapper objectMapper,
        final MustacheFactory mustacheFactory,
        final Murmur3 murmur3,
        final Clock clock,
        final TimeoutCalculator timeoutCalculator,
        final int retentionDays
    ) {
        checkNotNull(mustacheFactory);
        checkArgument(retentionDays >= 0);

        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.murmur3 = checkNotNull(murmur3);
        this.clock = checkNotNull(clock);
        this.timeoutCalculator = checkNotNull(timeoutCalculator);
        this.retentionDays = retentionDays;
        this.itemsKeyFormat = mustacheFactory.compile(new StringReader(ITEMS_KEY_FORMAT), "");
        this.itemKeyFormat = mustacheFactory.compile(new StringReader(ITEM_KEY_FORMAT), "");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public MessageLogId add(
        @Nonnull final MessageLog messageLog
    ) throws JsonProcessingException {
        checkNotNull(messageLog);

        final String dt = LocalDateTime.now(clock).format(FORMATTER);
        final String item = objectMapper.writeValueAsString(messageLog);
        final String hash = murmur3.hash128(item);
        final String listKey = buildListKey(dt);
        final String itemKey = buildItemKey(dt, hash);
        final long timeout = timeoutCalculator.calc(retentionDays);

        redisTemplate.opsForList().rightPush(listKey, hash);
        redisTemplate.expire(listKey, timeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(itemKey, item, timeout, TimeUnit.SECONDS);

        return new MessageLogId(dt, hash);
    }

    public int count() {
        final var keys = buildListKeys();
        return keys.stream()
            .map(key ->
                Optional
                    .ofNullable(redisTemplate.opsForList().size(key))
                    .orElse(0L))
            .mapToInt(Long::intValue)
            .sum();
    }

    public List<MessageLogId> getIds(int offset, int limit) {
        final int maxItemLength = offset + limit;
        final ArrayList<MessageLogId> itemKeys = Lists.newArrayListWithCapacity(maxItemLength);

        final List<String> listKeys = buildListKeys();
        for (final String listKey : listKeys) {
            if (itemKeys.size() >= maxItemLength) {
                break;
            }

            final List<String> hashes = redisTemplate.opsForList().range(listKey, 0, -1);
            if (CollectionUtils.isNotEmpty(hashes)) {
                final String dt = parseListKey(listKey).getDt();
                final List<MessageLogId> keys =
                    hashes.stream()
                        .map(hash -> new MessageLogId(dt, hash))
                        .collect(Collectors.toUnmodifiableList());
                itemKeys.addAll(keys);
            }
        }

        final int toIndex = Math.min(offset + limit, itemKeys.size());
        return itemKeys.subList(offset, toIndex);
    }

    @SuppressWarnings("UnstableApiUsage")
    public List<Pair<MessageLogId, MessageLog>> get(
        @Nonnull final List<MessageLogId> ids
    ) throws IOException {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }

        final List<String> keys = ids.stream()
            .map(id -> buildItemKey(id.getDt(), id.getHash()))
            .collect(Collectors.toUnmodifiableList());

        final List<String> values = redisTemplate.opsForValue().multiGet(keys);
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }


        final var builder =
            ImmutableList.<Pair<MessageLogId, MessageLog>>builderWithExpectedSize(values.size());
        for (int i = 0; i < ids.size(); ++i) {
            if (values.get(i) != null) {
                final var messageLog = objectMapper.readValue(values.get(i), MessageLog.class);
                builder.add(Pair.of(ids.get(i), messageLog));
            }
        }

        return builder.build();
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    String buildItemKey(
        final String dt,
        final String hash
    ) {
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(DT_KEY, dt, HASH_KEY, hash);
        itemKeyFormat.execute(writer, scopes);

        return writer.toString();
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    String buildListKey(
        final String dt
    ) {
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(DT_KEY, dt);
        itemsKeyFormat.execute(writer, scopes);

        return writer.toString();
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    List<String> buildListKeys() {
        final var now = LocalDateTime.now(clock);
        return IntStream
            .rangeClosed(0, retentionDays)
            .boxed()
            .map(days -> now.plusDays(days).format(FORMATTER))
            .map(this::buildListKey)
            .collect(Collectors.toUnmodifiableList());
    }

    @VisibleForTesting
    MessageLogId parseListKey(final String key) {
        final String dt = key.substring(ITEMS_KEY_PREFIX.length());
        return new MessageLogId(dt, null);
    }
}
