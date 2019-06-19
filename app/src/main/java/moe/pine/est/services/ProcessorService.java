package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.NotifyRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessorService {
    private final CompositeProcessor processor;

    public List<NotifyRequest> execute(final EmailMessage message) {
        Objects.requireNonNull(message);

        return processor.execute(message);
    }
}
