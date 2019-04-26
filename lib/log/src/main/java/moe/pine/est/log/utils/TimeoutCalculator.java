package moe.pine.est.log.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class TimeoutCalculator {
    private final Clock clock;

    public long calc(int retentionDays) {
        final var now = LocalDateTime.now(clock);
        final var expiredAt = now
                .plus(retentionDays + 1, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.DAYS);

        return ChronoUnit.SECONDS.between(now, expiredAt);
    }
}
