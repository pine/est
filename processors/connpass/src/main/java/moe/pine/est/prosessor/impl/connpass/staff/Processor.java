package moe.pine.est.prosessor.impl.connpass.staff;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.connpass.ConnpassNotification;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.processor.NotifyRequest;
import moe.pine.est.processor.ProcessorEnabled;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ProcessorEnabled
@ToString
@Slf4j
public class Processor implements moe.pine.est.processor.Processor {
    private static final String CONNPASS_EMAIL = "no-reply@connpass.com";

    private final List<ConnpassNotification> notifications;
    private final MessageParser messageParser;
    private final NotificationFactory notificationFactory;

    public Processor(
        @Qualifier("connpassNotifications") final List<ConnpassNotification> notifications,
        final MessageParser messageParser,
        final NotificationFactory notificationFactory
    ) {
        this.notifications = Objects.requireNonNull(notifications);
        this.messageParser = Objects.requireNonNull(messageParser);
        this.notificationFactory = Objects.requireNonNull(notificationFactory);
    }

    @Override
    public List<NotifyRequest> execute(final EmailMessage message) {
        Objects.requireNonNull(message);

        if (!message.getFrom().contains(CONNPASS_EMAIL)) {
            return Collections.emptyList();
        }

        return notifications
            .stream()
            .flatMap(connpassNotification -> {
                final var parserResult =
                    messageParser.parse(message, connpassNotification.getGroupId());
                if (parserResult == null) {
                    return Stream.empty();
                }

                final var notifyRequests =
                    notificationFactory.create(
                        parserResult,
                        connpassNotification.getNotificationGroupIds());
                if (CollectionUtils.isEmpty(notifyRequests)) {
                    return Stream.empty();
                }

                return notifyRequests.stream();
            })
            .collect(Collectors.toUnmodifiableList());
    }
}
