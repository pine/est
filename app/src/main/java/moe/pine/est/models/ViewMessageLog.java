package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ViewMessageLog {
    private String recipient;
    private String sender;
    private String from;
    private String subject;
    private String bodyPlain;
    private String strippedText;
    private String strippedSignature;
    private String bodyHtml;
    private String strippedHtml;
    private final LocalDateTime timestamp;
    private String token;
    private String signature;
}
