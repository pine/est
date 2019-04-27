package moe.pine.est.log.utils;

import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Component
public class TimeoutCalculator {
    @Nonnull
    private final Clock clock;

    public TimeoutCalculator(@Nonnull final Clock clock) {
        this.clock = checkNotNull(clock);
    }

    public long calc(int retentionDays) {
        checkArgument(retentionDays >= 0);

        final var now = LocalDateTime.now(clock);
        final var expiredAt = now
            .plus(retentionDays + 1, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS);

        return ChronoUnit.SECONDS.between(now, expiredAt);
    }
}
