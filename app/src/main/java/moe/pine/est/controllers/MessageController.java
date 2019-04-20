package moe.pine.est.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.mailgun.models.Message;
import moe.pine.est.models.MessageRequest;
import moe.pine.est.services.InvalidSignatureException;
import moe.pine.est.services.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    @Nonnull
    private MessageService messageService;

    @PostMapping("/api/messages")
    public void create(
        @Nonnull final MessageRequest messageRequest,
        @Nonnull final HttpServletResponse response
    ) throws IOException {
        log.info("New message received :: from=\"{}\", subject=\"{}\"",
            messageRequest.getFrom(), messageRequest.getSubject());

        final Message message = messageService.create(messageRequest);
        try {
            messageService.verify(message);
        } catch (final InvalidSignatureException e) {
            response.sendError(BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase());
            return;
        }

        response.getWriter().write(OK.getReasonPhrase());
    }
}
