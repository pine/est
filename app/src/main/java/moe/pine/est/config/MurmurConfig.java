package moe.pine.est.config;

import moe.pine.est.murmur.Murmur3;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MurmurConfig {
    private static final long MURMUR3_SEED = 0L;

    @Bean
    public Murmur3 murmur3() {
        return new Murmur3(MURMUR3_SEED);
    }
}
