package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
@ConfigurationProperties("slack")
public class SlackProperties {
    private @NotNull List<NotificationGroup> notificationGroups;

    @Data
    @Validated
    public static class NotificationGroup {
        private @NotBlank String id;
        private @NotBlank String token;
        private @NotBlank String channel;
        private String username;
        private String iconUrl;
    }
}
