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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
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

    public MessageLogRepository(
            final RedisTemplate<String, String> redisTemplate,
            final ObjectMapper objectMapper,
            final MustacheFactory mustacheFactory,
            final Murmur3 murmur3,
            final Clock clock,
            @Qualifier("messageLogKeyFormat") final String keyFormat
    ) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.murmur3 = checkNotNull(murmur3);
        this.clock = checkNotNull(clock);
        this.keyFormat = mustacheFactory.compile(new StringReader(keyFormat), "");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void add(@Nonnull final MessageLog messageLog)
            throws JsonProcessingException {
        checkNotNull(messageLog);

        final LocalDate dt = LocalDate.now(clock);
        final String item = objectMapper.writeValueAsString(messageLog);
        final String hash = murmur3.hash128(item);
        final String itemsKey = computeItemsKey(dt);
        final String itemKey = computeItemKey(dt, hash);

        log.debug("items-key={}, item-key={}, value={}", itemsKey, itemKey, item);

        // redisTemplate.opsForValue().set(key, value);
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    String computeItemKey(
            final LocalDate dt,
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
            final LocalDate dt
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
