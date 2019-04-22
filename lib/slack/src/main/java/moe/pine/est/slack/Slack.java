package moe.pine.est.slack;

import lombok.RequiredArgsConstructor;
import moe.pine.est.slack.models.SlackMessage;
import moe.pine.est.slack.models.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

@Component
@RequiredArgsConstructor
public class Slack {
    @SuppressWarnings("WeakerAccess")
    public static final String SLACK_CHAT_POST_MESSAGE = "https://slack.com/api/chat.postMessage";

    @Nonnull
    private final RestTemplate restTemplate;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void postMessage(
            @Nonnull final SlackMessage message
    ) {
        checkNotNull(message);

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + message.getToken());

        final MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("channel", message.getChannel());
        values.add("text", message.getText());

        final HttpEntity<?> request = new HttpEntity<>(values, headers);
        final Status status = restTemplate.postForObject(SLACK_CHAT_POST_MESSAGE, request, Status.class);

        if (status == null) {
            throw new SlackException("Failed to call chat.postMessage API. An empty response received.");
        }
        if (!status.isOk()) {
            throw new SlackException(
                    String.format(
                            "Failed to call users.setPhoto API :: ok=%s, error=\"%s\"",
                            String.valueOf(status.isOk()),
                            status.getError()));
        }
    }
}
