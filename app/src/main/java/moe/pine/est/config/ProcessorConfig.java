package moe.pine.est.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.Processor;
import moe.pine.est.processor.ProcessorEnabled;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class ProcessorConfig {
    @Bean
    public CompositeProcessor compositeProcessor(
        @Nonnull final List<Processor> processors
    ) {
        final List<Processor> enabledProcessors =
            processors.stream()
                .filter(processor -> processor
                    .getClass()
                    .isAnnotationPresent(ProcessorEnabled.class))
                .collect(Collectors.toList());

        final var compositeProcessor = new CompositeProcessor(enabledProcessors);
        log.info("CompositeProcessor created :: {}", compositeProcessor);

        return compositeProcessor;

    }
}
