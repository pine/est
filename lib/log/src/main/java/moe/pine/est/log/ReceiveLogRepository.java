package moe.pine.est.log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.HashFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.ReceiveLog;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
@Slf4j
public class ReceiveLogRepository {
    private final RedisTemplate redisTemplate;

    @SuppressWarnings("UnstableApiUsage")
    private final HashFunction hashFunction;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void add(@Nonnull final ReceiveLog receiveLog)
            throws JsonProcessingException {
        checkNotNull(receiveLog);

        final String receiveLogJson =
                objectMapper.writeValueAsString(receiveLog);

        final String hash = hashFunction
                .newHasher()
                .putString(receiveLogJson, StandardCharsets.UTF_8)
                .hash()
                .toString();

        log.debug("json={}, hash={}", receiveLogJson, hash);
    }
}
