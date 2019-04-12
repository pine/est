package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filter.FilterGroup;
import moe.pine.est.mailgun.Mailgun;
import moe.pine.est.mailgun.Message;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
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
    public String receive() {
        log.infov("{0}", filterGroup);

        filterGroup.doFilter(Message.builder().build());
        return "OK";
    }
}
