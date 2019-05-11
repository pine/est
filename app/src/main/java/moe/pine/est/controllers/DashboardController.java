package moe.pine.est.controllers;

import lombok.RequiredArgsConstructor;
import moe.pine.est.converters.ViewLogConverter;
import moe.pine.est.models.ViewPager;
import moe.pine.est.services.LogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Nonnull;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private static final int PER_PAGE = 50;

    private final LogService logService;
    private final ViewLogConverter viewLogConverter;

    @GetMapping("/dashboard")
    public String dashboard(
        @Nonnull final Model model
    ) {
        final int page = 0;
        final int maxItems = logService.count();
        final var items = logService
            .filter(PER_PAGE * page, PER_PAGE)
            .stream()
            .map(viewLogConverter::convert)
            .collect(Collectors.toUnmodifiableList());

        final ViewPager pager = ViewPager.builder()
            .perPage(PER_PAGE)
            .maxItems(maxItems)
            .build();

        model.addAttribute("items", items);
        model.addAttribute("pager", pager);
        return "dashboard";
    }
}
