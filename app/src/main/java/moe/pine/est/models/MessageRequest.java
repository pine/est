package moe.pine.est.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageRequest {
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
    private String messageHeaders;
}
