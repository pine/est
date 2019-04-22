package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("slack")
public class SlackProperties {
    private List<NotificationGroup> notificationGroups;

    @Data
    public static class NotificationGroup {
        private String id;
        private String token;
        private String channel;
    }
}
