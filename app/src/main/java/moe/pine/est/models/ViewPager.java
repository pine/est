package moe.pine.est.models;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Builder
public class ViewPager {
    private final int maxItems;
    private final int perPage;
    private final ViewPage page;

    @Nullable
    public ViewPage getPrevPage() {
        return paginate(page.getIndex() - 1);
    }

    @Nullable
    public ViewPage getNextPage() {
        return paginate(page.getIndex() + 1);
    }

    @Nullable
    public ViewPage paginate(int page) {
        if (page < 0) {
            return null;
        }
        if (page > getMaxPage()) {
            return null;
        }
        return ViewPage.builder().index(page).build();
    }

    public List<ViewPage> getPages() {
        return IntStream.rangeClosed(0, getMaxPage())
                .boxed()
                .flatMap(page ->
                        Optional.ofNullable(this.paginate(page))
                                .stream())
                .collect(Collectors.toUnmodifiableList());
    }

    public int getMaxPage() {
        return (maxItems - 1) / perPage + 1;
    }
}
