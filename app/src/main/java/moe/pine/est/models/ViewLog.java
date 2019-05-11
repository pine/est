package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViewLog {
    private final String dt;
    private final String hash;
    private final String subject;
    private final LocalDateTime timestamp;

    public String getPath() {
        return String.format("/messages/%s/%s", dt, hash);
    }
}
