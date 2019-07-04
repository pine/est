package moe.pine.est.log.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageLog {
    @Nullable
    private String recipient;

    @Nullable
    private String sender;

    @Nullable
    private String from;

    @Nullable
    private String subject;

    @Nullable
    private String bodyPlain;

    @Nullable
    private String strippedText;

    @Nullable
    private String strippedSignature;

    @Nullable
    private String bodyHtml;

    @Nullable
    private String strippedHtml;

    private long timestamp;
    private String token;
    private String signature;
}
