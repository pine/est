package moe.pine.est.converters;

import moe.pine.est.models.Log;
import moe.pine.est.models.ViewLog;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class ViewLogConverter {
    @Nonnull
    public ViewLog convert(@Nonnull final Log log) {
        return ViewLog.builder()
            .dt(log.getMessageLogId().getDt())
            .hash(log.getMessageLogId().getHash())
            .subject(log.getMessageLog().getSubject())
            .build();
    }
}
