package moe.pine.est.mailgun;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Message {
    private String recipient;
    private String sender;
    private String from;
    private String subject;
    private String bodyPlain;
    private MessageHeaders headers;
}
