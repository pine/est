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

    /*
    @FormParam("body-html") final String bodyHtml,
    @FormParam("stripped-html") final String strippedHtml,
    @FormParam("timestamp") final long timestamp,
    @FormParam("token") final String token,
    @FormParam("signature") final String signature,
    @FormParam("message-headers") final String messageHeaders

     */
}
