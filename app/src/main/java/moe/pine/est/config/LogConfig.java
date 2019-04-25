package moe.pine.est.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogConfig {
    @Bean
    public String messageLogKeyFormat() {
        return "message:{{dt}}{{#hash}}:{{hash}}{{/hash}}";
    }
}
