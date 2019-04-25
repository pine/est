package moe.pine.est.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.MessageLog;
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

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class MessageLogRepository {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYYMMdd");
    private static final String DT_KEY = "dt";
    private static final String HASH_KEY = "hash";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Murmur3 murmur3;
    private final Clock clock;
    private final Mustache keyFormat;
    private final int retentionDays;

    public MessageLogRepository(
        final RedisTemplate<String, String> redisTemplate,
        final ObjectMapper objectMapper,
        final MustacheFactory mustacheFactory,
        final Murmur3 murmur3,
        final Clock clock,
        final String keyFormat,
        final int retentionDays
    ) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.murmur3 = checkNotNull(murmur3);
        this.clock = checkNotNull(clock);
        this.keyFormat = mustacheFactory.compile(new StringReader(keyFormat), "");
        this.retentionDays = retentionDays;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void add(@Nonnull final MessageLog messageLog)
        throws JsonProcessingException {
        checkNotNull(messageLog);

        final var now = LocalDateTime.now(clock);
        final String item = objectMapper.writeValueAsString(messageLog);
        final String hash = murmur3.hash128(item);
        final String itemsKey = computeItemsKey(now);
        final String itemKey = computeItemKey(now, hash);

        final var expiredAt = now
            .plus(retentionDays + 1, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS);
        final long timeout = ChronoUnit.SECONDS.between(now, expiredAt);

        log.debug(
            "A message logged :: items-key={}, item-key={}, value={}, expired-at={}, timeout={}",
            itemsKey, itemKey, item, expiredAt, timeout);

        redisTemplate.opsForList().rightPush(itemsKey, hash);
        redisTemplate.expire(itemsKey, timeout, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(itemKey, item, timeout, TimeUnit.SECONDS);
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    String computeItemKey(
        final LocalDateTime dt,
        final String hash
    ) {
        final var writer = new StringWriter();
        final var scopes =
            ImmutableMap.of(
                DT_KEY, dt.format(FORMATTER),
                HASH_KEY, hash
            );
        keyFormat.execute(writer, scopes);

        return writer.toString();
    }

    private String computeItemsKey(
        final LocalDateTime dt
    ) {
        final var writer = new StringWriter();
        final var scopes =
            ImmutableMap.of(
                DT_KEY, dt.format(FORMATTER)
            );
        keyFormat.execute(writer, scopes);

        return writer.toString();
    }
}
