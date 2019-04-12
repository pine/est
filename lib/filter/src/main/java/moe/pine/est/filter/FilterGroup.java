package moe.pine.est.filter;

import lombok.Data;
import moe.pine.est.mailgun.Message;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class FilterGroup implements Filter {
    private List<Filter> filters;

    public FilterGroup() {
    }

    @Nonnull
    @Override
    public List<NotifyRequest> doFilter(@Nonnull final Message message) {
        if (CollectionUtils.isEmpty(filters)) {
            return Collections.emptyList();
        }

        return filters.stream()
                .flatMap(filter -> filter.doFilter(message).stream())
                .collect(Collectors.toList());
    }
}
