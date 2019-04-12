package moe.pine.est.mailgun;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String subject;
    private MessageHeaders headers;
}
