package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import moe.pine.est.processor.NotifyRequest;
import moe.pine.est.slack.models.SlackMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlackService {
    @Nonnull
    public SlackMessage newMessage(
        @Nonnull NotifyRequest notifyRequests
    ) {
        return SlackMessage.builder()
            .build();
    }

    public void postMessage(
        @Nonnull SlackMessage messages
    ) {

    }
}
