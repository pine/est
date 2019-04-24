package moe.pine.est.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.murmur.Murmur3;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MessageLogRepository {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYYMMdd");

    private final RedisTemplate<String, String> redisTemplate;
    private final Murmur3 murmur3;
    private final Clock clock;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String add(@Nonnull final MessageLog messageLog)
            throws JsonProcessingException {
        checkNotNull(messageLog);

        final String value = OBJECT_MAPPER.writeValueAsString(messageLog);
        final String id = murmur3.hash128(value);
        final String dt = LocalDate.now(clock).format(FORMATTER);
        final String key = String.format("msg:%s:%s", dt, id);

        log.debug("key={}, value={}", key, value);

        // redisTemplate.opsForValue().set(key, value);

        return id;
    }
}
