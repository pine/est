package moe.pine.est.producers;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.processor.CompositeProcessor;
import moe.pine.est.processor.Processor;
import moe.pine.est.processor.ProcessorEnabled;
import org.apache.commons.collections4.IterableUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.util.AnnotationLiteral;

@ApplicationScoped
@JBossLog
public class CompositeProcessorProducer {
    @Produces
    @ApplicationScoped
    public CompositeProcessor getCompositeProcessor(
        @Any Instance<Processor> instance
    ) {
        final var filters = IterableUtils.toList(
            instance.select(new AnnotationLiteral<ProcessorEnabled>() {
            }));
        final var compositeProcessor = new CompositeProcessor();
        compositeProcessor.setProcessors(filters);

        log.infov("CompositeProcessor created :: {0}", compositeProcessor);

        return compositeProcessor;
    }
}
