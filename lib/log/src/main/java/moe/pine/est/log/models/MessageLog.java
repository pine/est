package moe.pine.est.log.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageLog {
    private final String from;
    private final String subject;
    private long timestamp;
}
