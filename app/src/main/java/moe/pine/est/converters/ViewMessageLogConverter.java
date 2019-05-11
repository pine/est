package moe.pine.est.converters;

import moe.pine.est.log.models.MessageLog;
import moe.pine.est.models.ViewMessageLog;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Component
public class ViewMessageLogConverter {
    private static final ZoneOffset ZONE_OFFSET =
        ZoneId.of("Asia/Tokyo").getRules().getOffset(Instant.EPOCH);

    @Nonnull
    public ViewMessageLog convert(@Nonnull final MessageLog messageLog) {
        final var instant = Instant.ofEpochSecond(messageLog.getTimestamp());
        final var timestamp = LocalDateTime.ofInstant(instant, ZONE_OFFSET);

        return ViewMessageLog.builder()
            .from(messageLog.getFrom())
            .timestamp(timestamp)
            .build();
    }
}
