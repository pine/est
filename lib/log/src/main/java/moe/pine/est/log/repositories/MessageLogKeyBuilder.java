package moe.pine.est.log.repositories;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import moe.pine.est.log.models.MessageLogId;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class MessageLogKeyBuilder {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("YYYYMMdd");
    private static final String ITEMS_KEY_PREFIX = "message:";
    private static final String ITEMS_KEY_FORMAT = "message:{{dt}}";
    private static final String ITEM_KEY_FORMAT = "message:{{dt}}:{{hash}}";
    private static final String DT_KEY = "dt";
    private static final String HASH_KEY = "hash";

    private final Clock clock;
    private final int retentionDays;
    private final Mustache itemsKeyFormat;
    private final Mustache itemKeyFormat;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public MessageLogKeyBuilder(
        @Nonnull final MustacheFactory mustacheFactory,
        @Nonnull final Clock clock,
        final int retentionDays
    ) {
        checkNotNull(mustacheFactory);

        this.clock = checkNotNull(clock);
        this.retentionDays = retentionDays;
        this.itemsKeyFormat = mustacheFactory.compile(new StringReader(ITEMS_KEY_FORMAT), "");
        this.itemKeyFormat = mustacheFactory.compile(new StringReader(ITEM_KEY_FORMAT), "");
    }

    public String formattedDt() {
        return LocalDateTime.now(clock).format(FORMATTER);
    }

    public String buildItemKey(
        final String dt,
        final String hash
    ) {
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(DT_KEY, dt, HASH_KEY, hash);
        itemKeyFormat.execute(writer, scopes);

        return writer.toString();
    }

    public String buildListKey(
        final String dt
    ) {
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(DT_KEY, dt);
        itemsKeyFormat.execute(writer, scopes);

        return writer.toString();
    }

    public List<String> buildListKeys() {
        final var now = LocalDateTime.now(clock);
        return IntStream
            .rangeClosed(0, retentionDays)
            .boxed()
            .map(days -> now.minusDays(days).format(FORMATTER))
            .map(this::buildListKey)
            .collect(Collectors.toUnmodifiableList());
    }

    public MessageLogId parseListKey(final String key) {
        final String dt = key.substring(ITEMS_KEY_PREFIX.length());
        return new MessageLogId(dt, null);
    }
}
