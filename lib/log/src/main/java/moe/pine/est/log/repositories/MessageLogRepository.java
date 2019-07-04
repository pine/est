package moe.pine.est.log.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.log.utils.MessageLogKeyBuilder;
import moe.pine.est.log.utils.TimeoutCalculator;
import moe.pine.est.murmur.Murmur3;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
        final MessageLogKeyBuilder keyBuilder,
        final int retentionDays
    ) {
        checkArgument(retentionDays >= 0);

        this.redisTemplate = Objects.requireNonNull(redisTemplate);
        this.objectMapper = Objects.requireNonNull(objectMapper);
        this.murmur3 = Objects.requireNonNull(murmur3);
        this.timeoutCalculator = Objects.requireNonNull(timeoutCalculator);
        this.retentionDays = retentionDays;
        this.keyBuilder = Objects.requireNonNull(keyBuilder);
    }

    public MessageLogId add(
        final MessageLog messageLog
    ) throws JsonProcessingException {
        Objects.requireNonNull(messageLog);

        final String dt = keyBuilder.formattedDt();
        final String item = objectMapper.writeValueAsString(messageLog);
        final String hash = murmur3.hash128(item);
        final String listKey = keyBuilder.buildListKey(dt);
        final String itemKey = keyBuilder.buildItemKey(dt, hash);
        final long timeout = timeoutCalculator.calc(retentionDays);

        redisTemplate.opsForList().leftPush(listKey, hash);
        redisTemplate.expire(listKey, timeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(itemKey, item, timeout, TimeUnit.SECONDS);

        return new MessageLogId(dt, hash);
    }

    public int count() {
        final List<String> keys = keyBuilder.buildListKeys();
        log.debug("Count message logs from {}", keys);

        return keys.stream()
            .map(key ->
                Optional
                    .ofNullable(redisTemplate.opsForList().size(key))
                    .orElse(0L))
            .mapToInt(Long::intValue)
            .sum();
    }

    public List<MessageLogId> getIds(int offset, int limit) {
        checkArgument(offset >= 0,
            String.format("`offset` should be zero or more. The current value is %d.", offset));
        checkArgument(limit > 0,
            String.format("`limit` should be above zero. The current value is %d.", limit));

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
        return List.copyOf(itemKeys.subList(offset, toIndex));
    }

    @Nullable
    public MessageLog get(
        final MessageLogId id
    ) throws IOException {
        Objects.requireNonNull(id);
        
        final var ids = List.of(id);
        final var messageLogs = mget(ids);
        return messageLogs.isEmpty() ? null : messageLogs.get(0).getValue();
    }

    @SuppressWarnings("UnstableApiUsage")
    public List<Pair<MessageLogId, MessageLog>> mget(
        final List<MessageLogId> ids
    ) throws IOException {
        checkNotNull(ids);

        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final List<String> keys = ids.stream()
            .map(Objects::requireNonNull)
            .map(id -> {
                if (id.getHash() == null) {
                    throw new IllegalArgumentException(
                        String.format("`MessageLogId#hash` should not be empty. :: id = %s", id));
                }
                return keyBuilder.buildItemKey(id.getDt(), id.getHash());
            })
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
