package moe.pine.est.processor;

import moe.pine.est.email.models.EmailMessage;

import javax.annotation.Nonnull;
import java.util.List;

public interface Processor {
    @Nonnull
    List<NotifyRequest> execute(@Nonnull EmailMessage emailMessage);
}
