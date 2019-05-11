package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewLayout {
    private final String siteTitle;
    private final String pageTitle;
}
