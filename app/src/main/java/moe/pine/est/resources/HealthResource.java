package moe.pine.est.resources;

import moe.pine.est.filters.NoCache;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/")
public class HealthResource {
    @Context
    private ServletContext servletContext;

    @GET
    public Response home() {
        final String siteUrl = servletContext.getInitParameter("app.site-url");
        final URI redirectUrl = URI.create(siteUrl);
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
