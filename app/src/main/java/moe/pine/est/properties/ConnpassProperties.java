package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("connpass")
public class ConnpassProperties {
    private Staff staff;

    @Data
    public static class Staff {
        private MessageFormats messageFormats;
        private List<Notification> notifications;
    }

    @Data
    public static class MessageFormats {
        private String joinedFormat;
        private String leftFormat;
    }

    @Data
    public static class Notification {
        private String groupId;
        private List<String> notificationGroupIds;
    }
}
