package moe.pine.est.log.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class MessageLogRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Murmur3 murmur3;
    private final TimeoutCalculator timeoutCalculator;
    private final int retentionDays;
    private final MessageLogKeyBuilder keyBuilder;

    public MessageLogRepository(
        final RedisTemplate<String, String> redisTemplate,
        final ObjectMapper objectMapper,
        final Murmur3 murmur3,
        final TimeoutCalculator timeoutCalculator,
        final int retentionDays,
        final MessageLogKeyBuilder keyBuilder
    ) {
        checkArgument(retentionDays >= 0);

        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.murmur3 = checkNotNull(murmur3);
        this.timeoutCalculator = checkNotNull(timeoutCalculator);
        this.retentionDays = retentionDays;
        this.keyBuilder = checkNotNull(keyBuilder);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public MessageLogId add(
        @Nonnull final MessageLog messageLog
    ) throws JsonProcessingException {
        checkNotNull(messageLog);

        final String dt = keyBuilder.formattedDt();
        final String item = objectMapper.writeValueAsString(messageLog);
        final String hash = murmur3.hash128(item);
        final String listKey = keyBuilder.buildListKey(dt);
        final String itemKey = keyBuilder.buildItemKey(dt, hash);
        final long timeout = timeoutCalculator.calc(retentionDays);

        redisTemplate.opsForList().rightPush(listKey, hash);
        redisTemplate.expire(listKey, timeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(itemKey, item, timeout, TimeUnit.SECONDS);

        return new MessageLogId(dt, hash);
    }

    public int count() {
        final var keys = keyBuilder.buildListKeys();
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

        final List<String> listKeys = keyBuilder.buildListKeys();
        for (final String listKey : listKeys) {
            if (itemKeys.size() >= maxItemLength) {
                break;
            }

            final List<String> hashes = redisTemplate.opsForList().range(listKey, 0, -1);
            if (CollectionUtils.isNotEmpty(hashes)) {
                final String dt = keyBuilder.parseListKey(listKey).getDt();
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
            .map(id -> keyBuilder.buildItemKey(id.getDt(), id.getHash()))
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
}
