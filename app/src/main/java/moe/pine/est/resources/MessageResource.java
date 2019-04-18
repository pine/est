package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filter.FilterGroup;
import moe.pine.est.mailgun.Mailgun;
import moe.pine.est.mailgun.MessageRequest;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
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
        @BeanParam final MessageRequest messageRequest
    ) {
        log.infov(
            "Message received :: " +
                "recipient=\"{0}\", sender=\"{1}\", from=\"{2}\"",
            messageRequest.getRecipient(),
            messageRequest.getSender(),
            messageRequest.getFrom());

        //mailgun.receive()

//        mailgun.receive()

        // filterGroup.doFilter(Message.builder().build());

        return "OK";
    }
}
