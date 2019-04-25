package moe.pine.est.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.MustacheFactory;
import moe.pine.est.log.MessageLogRepository;
import moe.pine.est.murmur.Murmur3;
import moe.pine.est.properties.LogProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogConfig {
    @Bean
    public MessageLogRepository messageLogRepository(
        final RedisTemplate<String, String> redisTemplate,
        final ObjectMapper objectMapper,
        final MustacheFactory mustacheFactory,
        final Murmur3 murmur3,
        final Clock clock,
        final LogProperties logProperties
    ) {
        return new MessageLogRepository(
            redisTemplate,
            objectMapper,
            mustacheFactory,
            murmur3,
            clock,
            logProperties.getKeyFormats().getMessage(),
            logProperties.getRetentionDays()
        );
    }
}
