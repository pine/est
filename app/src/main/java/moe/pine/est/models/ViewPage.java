package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewPage {
    private final int page;

    public String getQuery() {
        if (page == 0) {
            return "";
        }
        return "?page=" + page;
    }
}
