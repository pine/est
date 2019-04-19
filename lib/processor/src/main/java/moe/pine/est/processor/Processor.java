package moe.pine.est.processor;

import javax.annotation.Nonnull;
import java.util.List;

public interface Processor {
    @Nonnull
    List<NotifyRequest> execute(@Nonnull Message message);
}
