package moe.pine.est.config;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import moe.pine.est.properties.AppProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfig {
    @Bean
    public MustacheFactory mustacheFactory() {
        return new DefaultMustacheFactory();
    }
}
