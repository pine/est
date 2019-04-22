package moe.pine.est.config;

import moe.pine.est.connpass.ConnpassNotification;
import moe.pine.est.connpass.MessageFormats;
import moe.pine.est.properties.ConnpassProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties(ConnpassProperties.class)
public class ConnpassConfig {
    @Bean
    public List<ConnpassNotification> connpassNotifications(
            @Nonnull ConnpassProperties connpassProperties
    ) {
        return connpassProperties
                .getStaff()
                .getNotifications()
                .stream()
                .map(notification ->
                        ConnpassNotification
                                .builder()
                                .groupId(notification.getGroupId())
                                .notificationGroupIds(notification.getNotificationGroupIds())
                                .build())
                .collect(Collectors.toUnmodifiableList());
    }

    @Bean
    public MessageFormats messageFormats(
            @Nonnull final ConnpassProperties connpassProperties
    ) {
        final var messageFormats = connpassProperties.getStaff().getMessageFormats();
        return new MessageFormats(
                messageFormats.getJoinedFormat(),
                messageFormats.getLeftFormat()
        );
    }
}