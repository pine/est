package moe.pine.est.log.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

public class TimeoutCalculatorTest {
    private static final ZoneId ZONE_ID = ZoneId.of("Asia/Tokyo");
    private static final LocalDateTime DATE_1 = LocalDateTime.of(2019, 4, 26, 19, 12, 34, 0);
    private static final LocalDateTime DATE_2 = LocalDateTime.of(2019, 4, 26, 0, 0, 0, 0);

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private TimeoutCalculator timeoutCalculator1;
    private TimeoutCalculator timeoutCalculator2;

    @Before
    public void setUp() {
        final var zoneOffset = ZONE_ID.getRules().getOffset(Instant.EPOCH);
        final var clock1 = Clock.fixed(DATE_1.toInstant(zoneOffset), ZONE_ID);
        final var clock2 = Clock.fixed(DATE_2.toInstant(zoneOffset), ZONE_ID);
        timeoutCalculator1 = new TimeoutCalculator(clock1);
        timeoutCalculator2 = new TimeoutCalculator(clock2);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_null() {
        expectedException.expect(NullPointerException.class);
        new TimeoutCalculator(null);
    }

    @Test
    @SuppressWarnings("PointlessArithmeticExpression")
    public void calcTest() {
        // 4 hours 47 minutes 26 seconds
        assertEquals((4 * 60 + 47) * 60 + 26L, timeoutCalculator1.calc(0));

        // 1 day 4 hours 47 minutes 26 seconds
        assertEquals(((1 * 24 + 4) * 60 + 47) * 60 + 26L, timeoutCalculator1.calc(1));

        // 1 day
        assertEquals(1 * 24 * 60 * 60, timeoutCalculator2.calc(0));

        // 2 day
        assertEquals(2 * 24 * 60 * 60, timeoutCalculator2.calc(1));
    }

    @Test
    public void calcTest_illegalArgument() {
        expectedException.expect(IllegalArgumentException.class);
        timeoutCalculator1.calc(-1);
    }
}
