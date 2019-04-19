package moe.pine.est.filters;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Locale;

import static javax.ws.rs.core.Response.Status.OK;

@Provider
@Cache
public class CacheFilter implements ContainerResponseFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(
        ContainerRequestContext requestContext,
        ContainerResponseContext responseContext) {
        if (responseContext.getStatusInfo() != OK) {
            return;
        }

        final Method method = resourceInfo.getResourceMethod();
        if (method == null) {
            return;
        }

        final Cache annotation = method.getAnnotation(Cache.class);
        final long maxAge = annotation.maxAge();
        if (maxAge == Long.MIN_VALUE) {
            return;
        }

        responseContext
            .getHeaders()
            .add(HttpHeaders.CACHE_CONTROL,
                String.format(Locale.US, "max-age=%d", maxAge));
    }
}
