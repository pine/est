package moe.pine.est.interceptors;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

@Provider
@NoCache
public class NoCacheFilter implements ContainerResponseFilter {
    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) {
        responseContext
                .getHeaders()
                .add(HttpHeaders.CACHE_CONTROL,
                        "private, no-cache, no-store, must-revalidate");
    }
}
