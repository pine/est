package moe.pine.est.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.log.MessageLogRepository;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.processor.NotifyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final MessageLogRepository receiveLogRepository;

    public void add(
            @Nonnull final EmailMessage message,
            @Nonnull final List<NotifyRequest> notifyRequests
    ) throws JsonProcessingException {
        receiveLogRepository.add(createLog(message, notifyRequests));
    }

    private MessageLog createLog(
            @Nonnull final EmailMessage message,
            @Nonnull final List<NotifyRequest> notifyRequests
    ) {
        return MessageLog.builder()
                .from(message.getFrom())
                .subject(message.getSubject())
                .build();
    }
}
