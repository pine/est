package moe.pine.est.mailgun;

import lombok.Data;

import javax.ws.rs.FormParam;

@Data
public class MessageRequest {
    @FormParam("recipient")
    private String recipient;

    @FormParam("sender")
    private String sender;

    @FormParam("from")
    private String from;

    @FormParam("subject")
    private String subject;

    @FormParam("body-plain")
    private String bodyPlain;

    @FormParam("stripped-text")
    private String strippedText;

    @FormParam("stripped-signature")
    private String strippedSignature;

    @FormParam("body-html")
    private String bodyHtml;

    @FormParam("stripped-html")
    private String strippedHtml;

    @FormParam("timestamp")
    private long timestamp;

    @FormParam("token")
    private String token;

    @FormParam("signature")
    private String signature;

    @FormParam("message-headers")
    private String messageHeaders;
}
