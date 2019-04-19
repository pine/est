package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.mailgun.models.Message;
import moe.pine.est.mailgun.models.MessageRequest;
import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.NotifyRequest;
import moe.pine.est.services.MailgunService;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

@Path("/api/messages")
@Dependent
@JBossLog
public class MessageResource {
    @Inject
    private MailgunService mailgunService;

    @Inject
    private CompositeProcessor processor;

    @POST
    public String receive(
        @BeanParam final MessageRequest messageRequest
    ) {
        final Message message = mailgunService.receive(messageRequest);
        final List<NotifyRequest> notifyRequests = processor.execute(message);

        return "OK";
    }
}
