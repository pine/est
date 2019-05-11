package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViewMessageLog {
    private final String from;
    private final LocalDateTime timestamp;
}
