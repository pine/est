package moe.pine.est.controllers;


import lombok.RequiredArgsConstructor;
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
public class HealthController {
    @Nonnull
    private AppProperties appProperties;

    @GetMapping("/")
    public void home(final HttpServletResponse response) throws IOException {
        final String siteUrl = appProperties.getSiteUrl();
        if (StringUtils.isEmpty(siteUrl)) {
            response.sendError(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
            return;
        }

        response.sendRedirect(siteUrl);
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}