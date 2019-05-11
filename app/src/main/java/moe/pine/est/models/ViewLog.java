package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewLog {
    private final String dt;
    private final String hash;
    private final String subject;

    public String getPath() {
        return String.format("/messages/%s/%s", dt, hash);
    }
}
