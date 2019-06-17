package moe.pine.est.controllers;

import lombok.RequiredArgsConstructor;
import moe.pine.est.converters.ViewLogConverter;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.models.ViewLayout;
import moe.pine.est.models.ViewLog;
import moe.pine.est.models.ViewPager;
import moe.pine.est.properties.AppProperties;
import moe.pine.est.services.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MessageController {
    private static final int PER_PAGE = 50;

    private final AppProperties appProperties;
    private final LogService logService;
    private final ViewLogConverter viewLogConverter;

    /*
    @GetMapping("/messages")
    public String index(
        @Nonnull final Model model
    ) {
        final int page = 0;
        final int maxItems = logService.count();
        final var logs = logService
            .filter(PER_PAGE * page, PER_PAGE)
            .stream()
            .map(viewLogConverter::convert)
            .collect(Collectors.toUnmodifiableList());

        final ViewPager pager = ViewPager.builder()
            .perPage(PER_PAGE)
            .maxItems(maxItems)
            .build();

        final ViewLayout layout = ViewLayout.builder()
            .siteTitle(appProperties.getSiteTitle())
            .pageTitle("")
            .build();

        model.addAttribute("layout", layout);
        model.addAttribute("logs", logs);
        model.addAttribute("pager", pager);
        return "messages";
    }

    @GetMapping("/messages/{dt}/{hash}")
    public String detail(
        @Nonnull final Model model,
        @Nonnull @PathVariable("dt") final String dt,
        @Nonnull @PathVariable("hash") final String hash
    ) {

        final MessageLogId messageLogId = new MessageLogId(dt, hash);
        final ViewLog log = viewLogConverter.convert(logService.find(messageLogId));
        final ViewLayout layout = ViewLayout.builder()
            .siteTitle(appProperties.getSiteTitle())
            .pageTitle(log.getMessageLog().getSubject())
            .build();

        model.addAttribute("layout", layout);
        model.addAttribute("log", log);
        model.addAttribute("dt", dt);
        model.addAttribute("hash", hash);
        return "messages_detail";
    }
     */
}
