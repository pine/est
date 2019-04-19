package moe.pine.est.properties;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jbosslog.JBossLog;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@ApplicationScoped
@Data
@JBossLog
public class SlackProperties {
    private static final String SLACK_NOTIFICATION_GROUP_KEY_FORMAT = "slack.notification-groups.%d.%s";
    private static final String SLACK_NOTIFICATION_GROUP_TOKEN_KEY = "token";
    private static final String SLACK_NOTIFICATION_GROUP_CHANNEL_KEY = "channel";
    private static final Pattern SLACK_NOTIFICATION_GROUP_PATTERN =
        Pattern.compile("slack\\.notification-groups\\.(?<index>\\d+)\\..+");

    private List<NotificationGroup> notificationGroups;

    @Data
    @Builder
    public static class NotificationGroup {
        private String token;
        private String channel;
        private String iconUrl;
    }

    @PostConstruct
    private void init() {
        final long length =
            System.getProperties()
                .stringPropertyNames()
                .stream()
                .flatMap(propertyName -> {
                    final var matcher = SLACK_NOTIFICATION_GROUP_PATTERN.matcher(propertyName);
                    if (!matcher.matches()) {
                        return Stream.empty();
                    }
                    final Integer index = Integer.valueOf(matcher.group("index"));
                    return Stream.ofNullable(index);
                })
                .distinct()
                .count();

        final List<NotificationGroup> notificationGroups =
            LongStream.range(0L, length)
                .boxed()
                .map(index -> {
                    final String token = String.format(
                        SLACK_NOTIFICATION_GROUP_KEY_FORMAT, index, SLACK_NOTIFICATION_GROUP_TOKEN_KEY);
                    final String channel = String.format(
                        SLACK_NOTIFICATION_GROUP_KEY_FORMAT, index, SLACK_NOTIFICATION_GROUP_CHANNEL_KEY);

                    return NotificationGroup.builder()
                        .token(System.getProperty(token))
                        .channel(System.getProperty(channel))
                        .build();
                })
                .collect(Collectors.toList());

        this.notificationGroups = notificationGroups;
    }
}
