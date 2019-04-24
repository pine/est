package moe.pine.est.config;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import moe.pine.est.murmur.Murmur3;
import moe.pine.est.properties.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MustacheFactory mustacheFactory() {
        return new DefaultMustacheFactory();
    }

    @Bean
    public Murmur3 murmur3() {
        return new Murmur3(0L);
    }
}
