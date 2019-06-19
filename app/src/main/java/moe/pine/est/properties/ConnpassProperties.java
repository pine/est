package moe.pine.est.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
@ConfigurationProperties("connpass")
public class ConnpassProperties {
    private @NotNull Staff staff;

    @Data
    @Validated
    public static class Staff {
        private @NotNull MessageFormats messageFormats;
        private @NotNull List<Notification> notifications;
    }

    @Data
    @Validated
    public static class MessageFormats {
        private @NotNull String joinedFormat;
        private @NotNull String leftFormat;
    }

    @Data
    @Validated
    public static class Notification {
        private @NotBlank String groupId;
        private @NotNull List<String> notificationGroupIds;
    }
}
