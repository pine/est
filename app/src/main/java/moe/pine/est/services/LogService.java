package moe.pine.est.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.log.ReceiveLogRepository;
import moe.pine.est.log.models.ReceiveLog;
import moe.pine.est.processor.NotifyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final ReceiveLogRepository receiveLogRepository;

    public void add(
            @Nonnull final EmailMessage message,
            @Nonnull final List<NotifyRequest> notifyRequests
    ) throws JsonProcessingException {
        receiveLogRepository.add(createLog(message, notifyRequests));
    }

    private ReceiveLog createLog(
            @Nonnull final EmailMessage message,
            @Nonnull final List<NotifyRequest> notifyRequests
    ) {
        return ReceiveLog.builder()
                .message(createLogMessage(message))
                .build();
    }

    private ReceiveLog.Message createLogMessage(
            @Nonnull final EmailMessage message
    ) {
        return ReceiveLog.Message.builder()
                .from(message.getFrom())
                .subject(message.getSubject())
                .build();
    }
}
