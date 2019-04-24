package moe.pine.est.config;

import com.google.common.hash.Hashing;
import moe.pine.est.log.ReceiveLogRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;

@Configuration
public class LogConfig {
    @Bean
    @SuppressWarnings("UnstableApiUsage")
    public ReceiveLogRepository logRepository(
            @Nonnull final RedisTemplate redisTemplate
    ) {
        return new ReceiveLogRepository(
                redisTemplate,
                Hashing.murmur3_128()
        );
    }
}
