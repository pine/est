package moe.pine.est.slack;

import moe.pine.est.slack.models.SlackMessage;
import moe.pine.est.slack.models.Status;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Objects;

public class Slack {
    static final Duration TIMEOUT = Duration.ofSeconds(60);
    static final String SLACK_CHAT_POST_MESSAGE = "https://slack.com/api/chat.postMessage";

    private final RestTemplate restTemplate;

    public Slack(final RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
            .setConnectTimeout(TIMEOUT)
            .setReadTimeout(TIMEOUT)
            .build();
    }

    public void postMessage(
        final SlackMessage message
    ) {
        Objects.requireNonNull(message);

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + message.getToken());

        final MultiValueMap<String, String> values = new LinkedMultiValueMap<>();
        values.add("channel", message.getChannel());
        values.add("username", message.getUsername());
        values.add("icon_url", message.getIconUrl());
        values.add("text", message.getText());

        final HttpEntity<?> request = new HttpEntity<>(values, headers);
        final Status status = restTemplate.postForObject(SLACK_CHAT_POST_MESSAGE, request, Status.class);

        if (status == null) {
            throw new SlackException("Failed to call `chat.postMessage` API. An empty response received.");
        }
        if (!status.isOk()) {
            throw new SlackException(
                String.format(
                    "Failed to call `chat.postMessage` API :: ok=%s, error=\"%s\"",
                    String.valueOf(status.isOk()),
                    status.getError()));
        }
    }
}
