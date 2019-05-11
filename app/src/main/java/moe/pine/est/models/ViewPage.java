package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewPage {
    private final int index;

    public String getQuery() {
        if (index == 0) {
            return "";
        }
        return "?page=" + index;
    }
}
