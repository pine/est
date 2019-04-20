package moe.pine.est.log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.Log;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LogRepository {
    // private final RedisTemplate redisTemplate;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void log(@Nonnull final Log log) {
        checkNotNull(log);
    }
}
