package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filters.Cache;
import moe.pine.est.filters.NoCache;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/")
@JBossLog
public class HealthResource {
    @Context
    private ServletContext servletContext;

    @GET
    @Cache(maxAge = 60 * 60 * 24)
    public Response home() {
        final String siteUrl = servletContext.getInitParameter("app.site-url");
        if (StringUtils.isEmpty(siteUrl)) {
            return Response.status(NOT_FOUND).build();
        }

        final URI redirectUrl = URI.create(siteUrl);
        log.debugv("Redirected :: redirect-url={0}", redirectUrl);

        return Response.seeOther(redirectUrl).build();
    }

    @Path("/health")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @NoCache
    public String health() {
        return "OK";
    }
}
