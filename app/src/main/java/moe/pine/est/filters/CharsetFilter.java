package moe.pine.est.filters;

import lombok.extern.jbosslog.JBossLog;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@JBossLog
public class CharsetFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        final var headers = requestContext.getHeaders();
        final var contentType = headers.getFirst("content-type");

        if (StringUtils.isEmpty(contentType)) {
            return;
        }
        if (contentType.contains("charset=")) {
            return;
        }

        headers.putSingle("content-type", contentType + "; charset=utf-8");
    }
}
