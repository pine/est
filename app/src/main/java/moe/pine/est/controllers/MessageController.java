package moe.pine.est.controllers;

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
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    @Nonnull
    private EmailService emailService;

    @Nonnull
    private ProcessorService processorService;

    @Nonnull
    private SlackService slackService;

    @Nonnull
    private LogService logService;

    @PostMapping("/api/messages")
    public void create(
        @Nonnull final MessageRequest messageRequest,
        @Nonnull final HttpServletResponse response
    ) throws IOException {
        log.info(
            "New email message received :: from=\"{}\", subject=\"{}\"",
            messageRequest.getFrom(),
            messageRequest.getSubject());

        log.debug(messageRequest.toString());

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

        notifyRequests
            .stream()
            .map(slackService::newMessage)
            .forEach(slackService::postMessage);

        logService.log(message, notifyRequests);
        response.getWriter().write(OK.getReasonPhrase());
    }
}
