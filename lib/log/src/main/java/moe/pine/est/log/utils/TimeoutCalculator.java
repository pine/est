package moe.pine.est.log.utils;

import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class TimeoutCalculator {
    private final Clock clock;

    public TimeoutCalculator(final Clock clock) {
        this.clock = Objects.requireNonNull(clock);
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
