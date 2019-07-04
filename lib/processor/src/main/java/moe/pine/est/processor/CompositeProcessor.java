package moe.pine.est.processor;

import lombok.RequiredArgsConstructor;
import moe.pine.est.email.models.EmailMessage;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CompositeProcessor implements Processor {
    private final List<Processor> processors;

    @Override
    public List<NotifyRequest> execute(
        final EmailMessage message
    ) {
        Objects.requireNonNull(message);

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

    @Override
    public String toString() {
        final String joinedProcessors =
            processors.stream()
                .map(v -> v.getClass().getCanonicalName())
                .collect(Collectors.joining(", "));
        return "CompositeProcessor(processors=[" + joinedProcessors + "])";
    }
}
