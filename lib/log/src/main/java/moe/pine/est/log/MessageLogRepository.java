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

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MessageLogRepository {
    private final RedisTemplate redisTemplate;
    private final Murmur3 murmur3;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public String add(@Nonnull final MessageLog receiveLog)
            throws JsonProcessingException {
        checkNotNull(receiveLog);

        final String json = objectMapper.writeValueAsString(receiveLog);
        final String id = murmur3.hash128(json);

        log.debug("json={}, hash={}", json, id);

        return id;
    }
}
