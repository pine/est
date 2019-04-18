package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filter.FilterGroup;
import moe.pine.est.mailgun.Mailgun;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api/messages")
@Dependent
@JBossLog
public class MessageResource {
    @Inject
    private Mailgun mailgun;

    @Inject
    private FilterGroup filterGroup;

    @POST
    public String receive(
        @FormParam("recipient") final String recipient,
        @FormParam("sender") final String sender,
        @FormParam("from") final String from,
        @FormParam("subject") final String subject,
        @FormParam("body-plain") final String bodyPlain
    ) {
        log.infov(
            "Message received :: recipient=\"{0}\", sender=\"{1}\", from=\"{2}\", subject=\"{3}\"",
            recipient, sender, from, subject);
        log.infov("{0}", bodyPlain);

        // filterGroup.doFilter(Message.builder().build());

        return "OK";
    }
}
