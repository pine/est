package moe.pine.est.converters;

import lombok.RequiredArgsConstructor;
import moe.pine.est.models.Log;
import moe.pine.est.models.ViewLog;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@RequiredArgsConstructor
public class ViewLogConverter {
    private final ViewMessageLogConverter viewMessageLogConverter;

    @Nonnull
    public ViewLog convert(@Nonnull final Log log) {

        final var messageLog = viewMessageLogConverter.convert(log.getMessageLog());

        return ViewLog.builder()
            .dt(log.getMessageLogId().getDt())
            .hash(log.getMessageLogId().getHash())
            .messageLog(messageLog)
            .build();
    }
}
