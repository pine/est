package moe.pine.est.processor;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import moe.pine.est.email.models.EmailMessage;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
@ToString
public class CompositeProcessor implements Processor {
    private final List<Processor> processors;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Nonnull
    @Override
    public List<NotifyRequest> execute(
            @Nonnull final EmailMessage message
    ) {
        checkNotNull(message);

        if (CollectionUtils.isEmpty(processors)) {
            return Collections.emptyList();
        }

        return processors.stream()
                .flatMap(processor -> {
                    final var notifyRequests = processor.execute(message);
                    if (CollectionUtils.isEmpty(notifyRequests)) {
                        return Stream.empty();
                    }
                    return notifyRequests.stream();
                })
                .collect(Collectors.toUnmodifiableList());
    }
}
