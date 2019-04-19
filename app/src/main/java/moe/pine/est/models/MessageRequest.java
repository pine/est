package moe.pine.est.models;

import lombok.Data;
import org.springframework.beans.factory.annotation.Qualifier;

@Data
public class MessageRequest {
    private String recipient;
    private String sender;
    private String from;
    private String subject;

    @Qualifier("body-plain")
    private String bodyPlain;

    private String strippedText;
    private String strippedSignature;
    private String bodyHtml;
    private String strippedHtml;
    private long timestamp;
    private String token;
    private String signature;
    private String messageHeaders;
}
