package moe.pine.est.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.properties.AppProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HealthController {
    @Nonnull
    private AppProperties appProperties;

    @GetMapping("/")
    public void home(
        @Nonnull final HttpServletResponse response
    ) throws IOException {
        final String redirectUrl = appProperties.getSiteUrl();
        if (StringUtils.isEmpty(redirectUrl)) {
            response.sendError(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
            return;
        }

        log.debug("Redirected :: redirect-url={}", redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}