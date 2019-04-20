package moe.pine.est.config;

import moe.pine.est.email.EmailVerifier;
import moe.pine.est.properties.MailgunProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

@Configuration
@EnableConfigurationProperties(MailgunProperties.class)
public class EmailConfig {
    @Bean
    public EmailVerifier emailVerifier(
        @Nonnull final MailgunProperties mailgunProperties
    ) {
        final String apiKey = mailgunProperties.getApiKey();
        if (StringUtils.isEmpty(apiKey)) {
            throw new RuntimeException("Mailgun api key is required.");
        }

        return new EmailVerifier(apiKey);
    }
}
