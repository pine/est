package moe.pine.est.converters;

import moe.pine.est.log.models.MessageLog;
import moe.pine.est.models.ViewMessageLog;
import org.apache.commons.lang3.StringUtils;
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
            .recipient(StringUtils.defaultString(messageLog.getRecipient()))
            .sender(StringUtils.defaultString(messageLog.getSender()))
            .from(StringUtils.defaultString(messageLog.getFrom()))
            .subject(StringUtils.defaultString(messageLog.getSubject()))
            .bodyPlain(StringUtils.defaultString(messageLog.getBodyPlain()))
            .strippedText(StringUtils.defaultString(messageLog.getStrippedText()))
            .strippedSignature(StringUtils.defaultString(messageLog.getStrippedSignature()))
            .bodyHtml(StringUtils.defaultString(messageLog.getBodyHtml()))
            .strippedHtml(StringUtils.defaultString(messageLog.getStrippedHtml()))
            .timestamp(timestamp)
            .token(StringUtils.defaultString(messageLog.getToken()))
            .signature(StringUtils.defaultString(messageLog.getSignature()))
            .build();
    }
}
