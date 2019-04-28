package moe.pine.est.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.MustacheFactory;
import moe.pine.est.log.repositories.MessageLogKeyBuilder;
import moe.pine.est.log.repositories.MessageLogRepository;
import moe.pine.est.log.repositories.NotifyRequestLogRepository;
import moe.pine.est.log.utils.TimeoutCalculator;
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
        final Murmur3 murmur3,
        final LogProperties logProperties,
        final TimeoutCalculator timeoutCalculator,
        final MessageLogKeyBuilder keyBuilder
    ) {
        return new MessageLogRepository(
            redisTemplate,
            objectMapper,
            murmur3,
            timeoutCalculator,
            logProperties.getRetentionDays(),
            keyBuilder
        );
    }

    @Bean
    public MessageLogKeyBuilder messageLogKeyBuilder(
        final MustacheFactory mustacheFactory,
        final Clock clock,
        final LogProperties logProperties
    ) {
        return new MessageLogKeyBuilder(
            mustacheFactory,
            clock,
            logProperties.getRetentionDays()
        );
    }

    @Bean
    public NotifyRequestLogRepository notifyRequestLogRepository(
        final RedisTemplate<String, String> redisTemplate,
        final ObjectMapper objectMapper,
        final MustacheFactory mustacheFactory,
        final TimeoutCalculator timeoutCalculator,
        final LogProperties logProperties
    ) {
        return new NotifyRequestLogRepository(
            redisTemplate,
            objectMapper,
            mustacheFactory,
            timeoutCalculator,
            logProperties.getRetentionDays()
        );
    }

}
