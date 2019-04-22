package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.log.LogRepository;
import moe.pine.est.processor.NotifyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final LogRepository logRepository;

    public void log(
        @Nonnull final EmailMessage message,
        @Nonnull final List<NotifyRequest> notifyRequests
    ) {
        // log.debug("message={}, notifyResults={}", message, notifyRequests);

        // TODO
    }
}
