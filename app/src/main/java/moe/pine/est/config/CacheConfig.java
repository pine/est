package moe.pine.est.config;

import moe.pine.spring.cache.interceptors.CacheInterceptor;
import moe.pine.spring.cache.interceptors.CachePolicy;
import moe.pine.spring.cache.interceptors.CachePolicyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {
    private static long MAX_AGE = 60 * 60 * 24; // 1 day

    @Bean
    public CacheInterceptor cacheInterceptor() {
        final CachePolicy cachePolicy =
            new CachePolicyBuilder()
                .public_()
                .maxAge(MAX_AGE)
                .build();

        return new CacheInterceptor(cachePolicy);
    }

    @Bean
    public CacheInterceptor noCacheInterceptor() {
        final CachePolicy cachePolicy =
            new CachePolicyBuilder()
                .private_()
                .noCache()
                .noStore()
                .mustRevalidate()
                .build();

        return new CacheInterceptor(cachePolicy);
    }
}
