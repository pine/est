package moe.pine.est.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.models.MessageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    @PostMapping("/api/messages")
    public String create(
        final MessageRequest messageRequest
    ) {
        log.info("New message received :: from={}, subject={}",
            messageRequest.getFrom(), messageRequest.getSubject());

        return "OK";
    }
}
