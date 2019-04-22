package moe.pine.est.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.email.models.EmailMessage;
import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.NotifyRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessorService {
    @Nonnull
    private final CompositeProcessor processor;

    @Nonnull
    public List<NotifyRequest> execute(final EmailMessage message) {
        checkNotNull(message);

        return processor.execute(message);
    }
}
