package moe.pine.est.log.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiveLog {
    private final Message message;

    @Data
    @Builder
    public static class Message {
        private final String from;
        private final String subject;
    }
}
