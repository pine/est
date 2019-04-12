package moe.pine.est.filter.impl.me;

import moe.pine.est.filter.Filter;
import moe.pine.est.filter.NotifyRequest;
import moe.pine.est.mailgun.Message;

import javax.annotation.Nonnull;
import javax.enterprise.context.ApplicationScoped;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class SagawaFilter implements Filter {
    @Override
    @Nonnull
    public List<NotifyRequest> doFilter(@Nonnull final Message message) {
        return Collections.emptyList();
    }
}
