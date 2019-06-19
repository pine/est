package moe.pine.est.controllers.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.InvalidSignatureException;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.models.MessageRequest;
import moe.pine.est.processor.NotifyRequest;
import moe.pine.est.services.EmailService;
import moe.pine.est.services.LogService;
import moe.pine.est.services.ProcessorService;
import moe.pine.est.services.SlackService;
import moe.pine.est.slack.models.SlackMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageApiController {
    private final EmailService emailService;
    private final ProcessorService processorService;
    private final SlackService slackService;
    private final LogService logService;

    @PostMapping("/api/messages")
    public void create(
        final MessageRequest messageRequest,
        final HttpServletResponse response
    ) throws IOException {
        log.info(
            "New email message received :: from=\"{}\", subject=\"{}\"",
            messageRequest.getFrom(),
            messageRequest.getSubject());

        final EmailMessage message = emailService.newMessage(messageRequest);
        try {
            emailService.verify(message);
        } catch (final InvalidSignatureException e) {
            response.sendError(BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase());
            return;
        }

        final List<NotifyRequest> notifyRequests = processorService.execute(message);
        if (CollectionUtils.isNotEmpty(notifyRequests)) {
            log.info("New notify requests created :: {}", notifyRequests);
        }

        final List<SlackMessage> slackMessages =
            notifyRequests
                .stream()
                .map(slackService::newMessage)
                .collect(Collectors.toUnmodifiableList());
        try {
            slackMessages.forEach(slackService::postMessage);
        } catch (final Throwable e) {
            log.error("Cannot send messages to Slack :: messages={}", slackMessages, e);
        }

        try {
            logService.add(message, notifyRequests);
        } catch (final Throwable e) {
            log.error("Cannot save logs :: message={}, notify-requests={}", message, notifyRequests, e);
        }

        response.getWriter().write(OK.getReasonPhrase());
    }
}
