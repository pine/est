package moe.pine.est.filter;

import moe.pine.est.mailgun.Message;

import javax.annotation.Nonnull;
import java.util.List;

public interface Filter {
    @Nonnull
    List<NotifyRequest> doFilter(@Nonnull Message message);
}
