package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@Validated
@ConfigurationProperties("app")
public class AppProperties {
    private @NotBlank String siteUrl;
    private @NotBlank String siteTitle;
    private @NotBlank String username;
    private @NotBlank String password;
}
