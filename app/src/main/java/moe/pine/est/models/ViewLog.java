package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ViewLog {
    private final String dt;
    private final String hash;
}
