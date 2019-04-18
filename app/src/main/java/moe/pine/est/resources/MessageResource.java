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
        @FormParam("sender") final String sender
    ) {
        log.infov(
            "Message received :: recipient={0}, sender={1}",
            recipient, sender);

        // filterGroup.doFilter(Message.builder().build());

        return "OK";
    }
}
