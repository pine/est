package moe.pine.est.processor;

import moe.pine.est.mailgun.models.Message;

import javax.annotation.Nonnull;
import java.util.List;

public interface Processor {
    @Nonnull
    List<NotifyRequest> execute(@Nonnull Message message);
}
