package moe.pine.est.prosessor.impl.connpass.staff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static moe.pine.est.prosessor.impl.connpass.staff.ParserResult.Action.JOINED;
import static moe.pine.est.prosessor.impl.connpass.staff.ParserResult.Action.LEFT;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageParser {
    private static final String CONNPASS_GROUP_URL = "https://%s.connpass.com/";
    private static final Pattern CONNPASS_EMAIL_NAME_PATTERN =
        Pattern.compile("\\>(?<name>[^\\<]+)\\<[a-z\\<\\>/]+さんが");
    private static final Pattern CONNPASS_EMAIL_JOINED_PATTERN =
        Pattern.compile("\\>(?<event>[^\\<]+)\\<[a-z\\<\\>/]+に参加登録");
    private static final Pattern CONNPASS_EMAIL_LEFT_PATTERN =
        Pattern.compile("\\>(?<event>[^\\<]+)\\<[a-z\\<\\>/]+」?への参加をキャンセル");

    private final ObjectMapper objectMapper;

    @Nullable
    @SneakyThrows(JsonProcessingException.class)
    public ParserResult parse(
        final EmailMessage message,
        final String groupId
    ) {
        final String bodyHtml =
            Optional.ofNullable(message.getBodyHtml())
                .filter(StringUtils::isNotEmpty)
                .orElse(StringUtils.EMPTY);
        if (StringUtils.isEmpty(bodyHtml)) {
            log.debug(
                "A message body is empty :: from={}, subject={}",
                objectMapper.writeValueAsString(message.getFrom()),
                objectMapper.writeValueAsString(message.getSubject()));
            return null;
        }

        final String groupUrl = String.format(CONNPASS_GROUP_URL, groupId);
        if (!bodyHtml.contains(groupUrl)) {
            return null;
        }
        log.debug(
            "A Connpass group detected :: group-id={}",
            objectMapper.writeValueAsString(groupId));

        final Matcher nameMatcher = CONNPASS_EMAIL_NAME_PATTERN.matcher(bodyHtml);
        if (!nameMatcher.find()) {
            return null;
        }

        final String name = nameMatcher.group("name");
        log.debug(
            "An attendee name found :: name={}",
            objectMapper.writeValueAsString(name));

        final Matcher joinedMatcher =
            CONNPASS_EMAIL_JOINED_PATTERN.matcher(bodyHtml);
        if (joinedMatcher.find()) {
            final String event = joinedMatcher.group("event");
            log.debug(
                "An event found from joined message :: event={}",
                objectMapper.writeValueAsString(event));

            return ParserResult.builder()
                .name(name)
                .event(event)
                .action(JOINED)
                .build();
        }

        final Matcher leftMatcher =
            CONNPASS_EMAIL_LEFT_PATTERN.matcher(bodyHtml);
        if (leftMatcher.find()) {
            final String event = leftMatcher.group("event");
            log.debug(
                "An event found from left message :: event={}",
                objectMapper.writeValueAsString(event));

            return ParserResult.builder()
                .name(name)
                .event(event)
                .action(LEFT)
                .build();
        }

        return null;
    }
}
