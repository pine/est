package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.processor.NotifyRequest;
import moe.pine.est.properties.SlackProperties;
import moe.pine.est.slack.Slack;
import moe.pine.est.slack.models.SlackMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {
    private final SlackProperties slackProperties;
    private final Slack slack;

    @Nonnull
    public SlackMessage newMessage(
            @Nonnull NotifyRequest notifyRequest
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
                .text(notifyRequest.getText())
                .build();
    }

    public void postMessage(
            @Nonnull SlackMessage message
    ) {
        slack.postMessage(checkNotNull(message));
    }
}
