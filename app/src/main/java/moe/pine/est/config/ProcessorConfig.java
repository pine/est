package moe.pine.est.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.Processor;
import moe.pine.est.utils.ProcessorUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Configuration
@Slf4j
public class ProcessorConfig {
    @Bean
    public CompositeProcessor compositeProcessor(
            @Nullable final List<Processor> processors,
            @Nonnull final ProcessorUtils processorUtils
    ) {
        final var enabledProcessors = processorUtils.filterEnabled(processors);
        if (CollectionUtils.isEmpty(enabledProcessors)) {
            log.warn("No enable processors detected.");
        }

        final var compositeProcessor = new CompositeProcessor(enabledProcessors);
        log.info("CompositeProcessor created :: {}", compositeProcessor);

        return compositeProcessor;
    }
}
