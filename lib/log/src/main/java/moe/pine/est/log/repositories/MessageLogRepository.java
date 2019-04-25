package moe.pine.est.log.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.MessageLogKey;
import moe.pine.est.murmur.Murmur3;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class MessageLogRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYYMMdd");
    private static final String ITEMS_KEY_FORMAT = "message:{{dt}}";
    private static final String ITEM_KEY_FORMAT = "message:{{dt}}::{{hash}}";
    private static final String DT_KEY = "dt";
    private static final String HASH_KEY = "hash";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Murmur3 murmur3;
    private final Clock clock;
    private final int retentionDays;
    private final Mustache itemsKeyFormat;
    private final Mustache itemKeyFormat;

    public MessageLogRepository(
        final RedisTemplate<String, String> redisTemplate,
        final ObjectMapper objectMapper,
        final MustacheFactory mustacheFactory,
        final Murmur3 murmur3,
        final Clock clock,
        final int retentionDays
    ) {
        checkArgument(retentionDays >= 0);

        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.murmur3 = checkNotNull(murmur3);
        this.clock = checkNotNull(clock);
        this.retentionDays = retentionDays;
        this.itemsKeyFormat = mustacheFactory.compile(new StringReader(ITEMS_KEY_FORMAT), "");
        this.itemKeyFormat = mustacheFactory.compile(new StringReader(ITEM_KEY_FORMAT), "");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public MessageLogKey add(@Nonnull final MessageLog messageLog)
        throws JsonProcessingException {
        checkNotNull(messageLog);

        final var now = LocalDateTime.now(clock);
        final String dt = now.format(FORMATTER);
        final String item = objectMapper.writeValueAsString(messageLog);
        final String hash = murmur3.hash128(item);
        final String itemsKey = computeItemsKey(dt);
        final String itemKey = computeItemKey(dt, hash);
        final long timeout = computeTimeout(now);

        redisTemplate.opsForList().rightPush(itemsKey, hash);
        redisTemplate.expire(itemsKey, timeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(itemKey, item, timeout, TimeUnit.SECONDS);

        return new MessageLogKey(dt, hash);
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    long computeTimeout(
        final LocalDateTime now
    ) {
        final var expiredAt = now
            .plus(retentionDays + 1, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS);
        return ChronoUnit.SECONDS.between(now, expiredAt);
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    String computeItemKey(
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
    String computeItemsKey(
        final String dt
    ) {
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(DT_KEY, dt);
        itemsKeyFormat.execute(writer, scopes);

        return writer.toString();
    }
}
