package moe.pine.est.converters;

import moe.pine.est.models.Log;
import moe.pine.est.models.ViewLog;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class ViewLogConverter {
    private static final ZoneOffset ZONE_OFFSET =
        ZoneId.of("Asia/Tokyo").getRules().getOffset(Instant.EPOCH);

    @Nonnull
    public ViewLog convert(@Nonnull final Log log) {
        final var instant = Instant.ofEpochSecond(log.getMessageLog().getTimestamp());
        final var timestamp = LocalDateTime.ofInstant(instant, ZONE_OFFSET);

        return ViewLog.builder()
            .dt(log.getMessageLogId().getDt())
            .hash(log.getMessageLogId().getHash())
            .subject(log.getMessageLog().getSubject())
            .timestamp(timestamp)
            .build();
    }
}
