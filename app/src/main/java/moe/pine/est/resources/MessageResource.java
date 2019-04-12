package moe.pine.est.resources;

import moe.pine.est.filter.Filters;
import moe.pine.est.mailgun.Mailgun;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api/messages")
@Dependent
public class MessageResource {
    @Inject
    private Mailgun mailgun;

    @Inject
    private Filters filters;

    @POST
    public String receive() {
        System.out.println(mailgun.toString());
        System.out.println(filters.toString());
        filters.foo();
        return "OK";
    }
}
