package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties("mailgun")
public class MailgunProperties {
    private @NotBlank String apiKey;
}
