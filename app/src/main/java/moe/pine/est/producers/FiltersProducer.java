package moe.pine.est.producers;

import lombok.extern.jbosslog.JBossLog;
import moe.pine.est.filter.Filter;
import moe.pine.est.filter.FilterGroup;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import java.util.stream.Collectors;

@ApplicationScoped
@JBossLog
public class FiltersProducer {
    @Produces
    @ApplicationScoped
    public FilterGroup getFilters(
            @Any Instance<Filter> instance
    ) {
        final var filters = instance.stream()
                .filter(filter -> !(filter instanceof FilterGroup))
                .collect(Collectors.toUnmodifiableList());

        final var filterGroup = new FilterGroup();
        filterGroup.setFilters(filters);

        log.infov("FilterGroup created :: {0}", filterGroup);

        return filterGroup;
    }
}
