package moe.pine.est.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.NotifyRequestLog;
import moe.pine.est.log.repositories.MessageLogRepository;
import moe.pine.est.log.repositories.NotifyRequestLogRepository;
import moe.pine.est.processor.NotifyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final MessageLogRepository messageLogRepository;
    private final NotifyRequestLogRepository notifyRequestLogRepository;

    public void add(
        @Nonnull final EmailMessage message,
        @Nonnull final List<NotifyRequest> notifyRequests
    ) throws JsonProcessingException {
        final var messageLog = createMessageLog(message);
        final var notifyRequestLogs =
            notifyRequests.stream()
                .map(this::createNotifyRequestLog)
                .collect(Collectors.toUnmodifiableList());

        final var messageLogKey = messageLogRepository.add(messageLog);
        notifyRequestLogRepository.add(messageLogKey, notifyRequestLogs);
    }

    private MessageLog createMessageLog(
        @Nonnull final EmailMessage message
    ) {
        return MessageLog.builder()
            .recipient(message.getRecipient())
            .sender(message.getSender())
            .from(message.getFrom())
            .subject(message.getSubject())
            .bodyPlain(message.getBodyPlain())
            .strippedText(message.getStrippedText())
            .strippedSignature(message.getStrippedSignature())
            .bodyHtml(message.getBodyHtml())
            .strippedHtml(message.getStrippedHtml())
            .timestamp(message.getTimestamp())
            .token(message.getToken())
            .signature(message.getSignature())
            .build();
    }

    private NotifyRequestLog createNotifyRequestLog(
        @Nonnull final NotifyRequest notifyRequest
    ) {
        return NotifyRequestLog.builder().build();
    }
}
