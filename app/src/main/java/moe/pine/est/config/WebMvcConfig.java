package moe.pine.est.config;

import lombok.RequiredArgsConstructor;
import moe.pine.est.interceptors.CacheInterceptor;
import moe.pine.est.interceptors.NoCacheInterceptor;
import moe.pine.est.controllers.MessageRequestArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final MessageRequestArgumentResolver messageRequestArgumentResolver;
    private final CacheInterceptor cacheInterceptor;
    private final NoCacheInterceptor noCacheInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(messageRequestArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry
            .addInterceptor(cacheInterceptor)
            .addPathPatterns("/");

        registry
            .addInterceptor(noCacheInterceptor)
            .addPathPatterns("/health")
            .addPathPatterns("/api/**");
    }
}