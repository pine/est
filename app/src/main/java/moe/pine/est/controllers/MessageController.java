package moe.pine.est.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.models.MessageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    @PostMapping("/api/messages")
    public String create(
        @RequestBody final MessageRequest messageRequest
    ) {
        log.info("message={}", messageRequest);

    }
}
