package moe.pine.est.filter;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class Filters {
    private Instance<Filter> filters;

    @Inject @Any
    public Filters(Instance<Filter> filters) {
        this.filters = filters;
    }

    public void foo() {
        System.out.println(filters.stream().collect(Collectors.toList()));
    }
}
