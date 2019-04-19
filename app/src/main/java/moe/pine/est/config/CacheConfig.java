package moe.pine.est.config;

import moe.pine.est.interceptors.CacheInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    private static long MAX_AGE = 60 * 60 * 24; // 1 day

    @Bean
    public CacheInterceptor cacheInterceptor() {
        return new CacheInterceptor(MAX_AGE);
    }
}
