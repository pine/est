package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties("log")
public class LogProperties {
    private @NotNull Integer retentionDays;
}
