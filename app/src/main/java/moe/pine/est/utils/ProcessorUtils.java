package moe.pine.est.utils;

import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.Processor;
import moe.pine.est.processor.ProcessorEnabled;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcessorUtils {
    public List<Processor> filterEnabled(
            @Nullable final List<Processor> processors
    ) {
        if (CollectionUtils.isEmpty(processors)) {
            return Collections.emptyList();
        }

        return processors.stream()
                .filter(processor -> processor
                        .getClass()
                        .isAnnotationPresent(ProcessorEnabled.class))
                .filter(processor -> !(processor instanceof CompositeProcessor))
                .collect(Collectors.toUnmodifiableList());
    }
}
