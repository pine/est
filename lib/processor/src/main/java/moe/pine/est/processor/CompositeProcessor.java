package moe.pine.est.processor;

import lombok.Data;
import lombok.NoArgsConstructor;
import moe.pine.est.mailgun.models.Message;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class CompositeProcessor implements Processor {
    private List<Processor> processors;

    @Nonnull
    @Override
    public List<NotifyRequest> execute(@Nonnull final Message message) {
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
            .collect(Collectors.toList());
    }
}
