package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("log")
public class LogProperties {
    private int retentionDays;
    private KeyFormats keyFormats;

    @Data
    public static class KeyFormats {
        private String message;
    }
}
