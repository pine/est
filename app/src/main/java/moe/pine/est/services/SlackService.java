package moe.pine.est.services;

import lombok.extern.slf4j.Slf4j;
import moe.pine.est.processor.NotifyRequest;
import moe.pine.est.properties.SlackProperties;
import moe.pine.est.slack.Slack;
import moe.pine.est.slack.models.SlackMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Slf4j
public class SlackService {
    private final SlackProperties slackProperties;
    private final Slack slack;
    private final ExecutorService executorService;

    public SlackService(
        final SlackProperties slackProperties,
        final Slack slack,
        @Qualifier("slackExecutorService") final ExecutorService executorService
    ) {
        this.slackProperties = Objects.requireNonNull(slackProperties);
        this.slack = Objects.requireNonNull(slack);
        this.executorService = Objects.requireNonNull(executorService);
    }

    public SlackMessage newMessage(
        final NotifyRequest notifyRequest
    ) {
        final String notificationGroupId = notifyRequest.getNotificationGroupId();
        if (StringUtils.isEmpty(notificationGroupId)) {
            throw new IllegalArgumentException("`notificationGroupId` should not be empty.");
        }

        final var notificationGroupOpt =
            slackProperties.getNotificationGroups()
                .stream()
                .filter(v -> StringUtils.equals(v.getId(), notificationGroupId))
                .findFirst();

        if (notificationGroupOpt.isEmpty()) {
            throw new RuntimeException(
                String.format(
                    "A notification group `%s` is not found.",
                    notificationGroupId));
        }

        final var notificationGroup = notificationGroupOpt.get();
        return SlackMessage.builder()
            .token(notificationGroup.getToken())
            .channel(notificationGroup.getChannel())
            .username(notificationGroup.getUsername())
            .iconUrl(notificationGroup.getIconUrl())
            .text(notifyRequest.getText())
            .build();
    }

    public void postMessage(
        final SlackMessage message
    ) {
        executorService.execute(() ->
            slack.postMessage(checkNotNull(message)));
    }
}
