package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.log.models.NotifyRequestLog;
import moe.pine.est.log.repositories.MessageLogRepository;
import moe.pine.est.log.repositories.NotifyRequestLogRepository;
import moe.pine.est.models.Log;
import moe.pine.est.processor.NotifyRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collections;
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
    ) throws IOException {
        final var messageLog = createMessageLog(message);
        final var notifyRequestLogs =
            notifyRequests.stream()
                .map(this::createNotifyRequestLog)
                .collect(Collectors.toUnmodifiableList());

        final var messageLogId = messageLogRepository.add(messageLog);
        notifyRequestLogRepository.add(messageLogId, notifyRequestLogs);
    }

    public List<Log> filter(int offset, int limit) {
        final List<MessageLogId> messageLogIds;
        final List<Pair<MessageLogId, MessageLog>> messageLogs;
        try {
            messageLogIds = messageLogRepository.getIds(offset, limit);
            messageLogs = messageLogRepository.mget(messageLogIds);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return messageLogs.stream()
            .map(v -> Log.builder()
                .messageLogId(v.getKey())
                .messageLog(v.getValue())
                .notifyRequestLogs(Collections.emptyList())
                .build())
            .collect(Collectors.toUnmodifiableList());
    }

    public int count() {
        return messageLogRepository.count();
    }

    public Log find(@Nonnull final MessageLogId messageLogId) {
        final MessageLog messageLog;
        try {
            messageLog = messageLogRepository.get(messageLogId);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return Log.builder()
            .messageLogId(messageLogId)
            .messageLog(messageLog)
            .build();
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
        return NotifyRequestLog.builder()
            .notificationGroupId(notifyRequest.getNotificationGroupId())
            .text(notifyRequest.getText())
            .build();
    }
}
