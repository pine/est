package moe.pine.est.resources;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filters.Cache;
import moe.pine.est.filters.NoCache;
import moe.pine.est.properties.AppProperties;
import moe.pine.est.properties.SlackProperties;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
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

    @Inject
    private AppProperties appProperties;

    @Inject
    private SlackProperties slackProperties;

    @GET
    @Cache(maxAge = 60 * 60 * 24)
    public Response home() {
        log.info(slackProperties);

        final String siteUrl = appProperties.getSiteUrl();
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
