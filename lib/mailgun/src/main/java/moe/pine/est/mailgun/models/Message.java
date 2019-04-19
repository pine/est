package moe.pine.est.mailgun.models;

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
    private String strippedText;
    private String strippedSignature;
    private String bodyHtml;
    private String strippedHtml;
    private long timestamp;
    private String token;
    private String signature;
    private MessageHeaders headers;
}