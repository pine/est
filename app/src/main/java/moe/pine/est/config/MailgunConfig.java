package moe.pine.est.config;

import moe.pine.est.properties.MailgunProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MailgunProperties.class)
public class MailgunConfig {
}
