package moe.pine.est.log.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.log.models.NotifyRequestLog;
import moe.pine.est.log.utils.TimeoutCalculator;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Nonnull;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class NotifyRequestLogRepository {
    private static final String ITEMS_KEY_FORMAT = "notify_requests:{{dt}}::{{hash}}";
    private static final String DT_KEY = "dt";
    private static final String HASH_KEY = "hash";

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final Mustache itemsKeyFormat;
    private final TimeoutCalculator timeoutCalculator;
    private final int retentionDays;

    public NotifyRequestLogRepository(
            final RedisTemplate<String, String> redisTemplate,
            final ObjectMapper objectMapper,
            final MustacheFactory mustacheFactory,
            final TimeoutCalculator timeoutCalculator,
            final int retentionDays
    ) {
        this.redisTemplate = checkNotNull(redisTemplate);
        this.objectMapper = checkNotNull(objectMapper);
        this.itemsKeyFormat = mustacheFactory.compile(new StringReader(ITEMS_KEY_FORMAT), "");
        this.timeoutCalculator = checkNotNull(timeoutCalculator);
        this.retentionDays = retentionDays;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void add(
            @Nonnull MessageLogId messageLogKey,
            @Nonnull List<NotifyRequestLog> notifyRequestLogs
    ) throws JsonProcessingException {
        checkNotNull(messageLogKey);
        checkNotNull(notifyRequestLogs);

        if (CollectionUtils.isEmpty(notifyRequestLogs)) {
            return;
        }

        final String item = objectMapper.writeValueAsString(notifyRequestLogs);
        final String itemsKey = buildKey(messageLogKey.getDt(), messageLogKey.getHash());
        final long timeout = timeoutCalculator.calc(retentionDays);

        redisTemplate.opsForValue().set(itemsKey, item, timeout, TimeUnit.SECONDS);
    }


    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    String buildKey(
            final String dt,
            final String hash
    ) {
        final var writer = new StringWriter();
        final var scopes = ImmutableMap.of(DT_KEY, dt, HASH_KEY, hash);
        itemsKeyFormat.execute(writer, scopes);

        return writer.toString();
    }
}
