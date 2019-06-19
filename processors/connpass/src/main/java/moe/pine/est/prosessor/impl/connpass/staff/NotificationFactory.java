package moe.pine.est.prosessor.impl.connpass.staff;

import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import moe.pine.est.connpass.MessageFormats;
import moe.pine.est.processor.NotifyRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;


@Component
@RequiredArgsConstructor
public class NotificationFactory {
    private static final String NAME_KEY = "name";
    private static final String EVENT_KEY = "event";

    private final MessageFormats messageFormats;
    private final MustacheFactory mustacheFactory;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Nonnull
    public List<NotifyRequest> create(
        @Nonnull ParserResult parserResult,
        @Nonnull List<String> notificationGroupIds
    ) {
        Objects.requireNonNull(parserResult);
        Objects.requireNonNull(notificationGroupIds);

        final String format;
        switch (parserResult.getAction()) {
            case JOINED:
                format = messageFormats.getJoinedFormat();
                break;
            case LEFT:
                format = messageFormats.getLeftFormat();
                break;
            default:
                throw new RuntimeException(
                    String.format(
                        "Illegal action type :: action=%s",
                        String.valueOf(parserResult.getAction())));
        }

        checkState(StringUtils.isNotEmpty(format));

        final var reader = new StringReader(format);
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(
            NAME_KEY, parserResult.getName(),
            EVENT_KEY, parserResult.getEvent()
        );

        final var mustache = mustacheFactory.compile(reader, "");
        mustache.execute(writer, scopes);

        return notificationGroupIds.stream()
            .map(notificationGroupId ->
                NotifyRequest.builder()
                    .text(writer.toString())
                    .notificationGroupId(notificationGroupId)
                    .build()
            )
            .collect(Collectors.toUnmodifiableList());
    }
}
