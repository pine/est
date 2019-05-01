package moe.pine.est.log.utils;

import com.github.mustachejava.DefaultMustacheFactory;
import com.google.common.collect.ImmutableList;
import moe.pine.est.log.repositories.MessageLogKeyBuilder;
import org.junit.Before;
import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageLogKeyBuilderTest {
    private static final LocalDateTime DATE_1 = LocalDateTime.of(2019, 4, 26, 19, 12, 34, 0);
    private static final LocalDateTime DATE_2 = LocalDateTime.of(2019, 4, 26, 0, 0, 0, 0);
    private static final int RETENTION_DAYS_1 = 3;
    private static final int RETENTION_DAYS_2 = 5;

    private MessageLogKeyBuilder keyBuilder1;
    private MessageLogKeyBuilder keyBuilder2;

    @Before
    public void setUp() {
        final var mustacheFactory = new DefaultMustacheFactory();
        final var zoneId = ZoneId.of("Asia/Tokyo");
        final var zoneOffset = zoneId.getRules().getOffset(Instant.EPOCH);
        final var clock1 = Clock.fixed(DATE_1.toInstant(zoneOffset), zoneId);
        final var clock2 = Clock.fixed(DATE_2.toInstant(zoneOffset), zoneId);
        keyBuilder1 = new MessageLogKeyBuilder(mustacheFactory, clock1, RETENTION_DAYS_1);
        keyBuilder2 = new MessageLogKeyBuilder(mustacheFactory, clock2, RETENTION_DAYS_2);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_nullMustacheFactory() {
        new MessageLogKeyBuilder(null, Clock.systemUTC(), RETENTION_DAYS_1);
    }

    @Test
    public void formattedDtTest() {
        assertEquals("20190426", keyBuilder1.formattedDt());
        assertEquals("20190426", keyBuilder2.formattedDt());
    }

    @Test
    public void buildItemKeyTest() {
        assertEquals("message:20190428:foo", keyBuilder1.buildItemKey("20190428", "foo"));
        assertEquals("message:20190428:foo", keyBuilder2.buildItemKey("20190428", "foo"));
    }

    @Test
    public void buildListKeyTest() {
        assertEquals("message:20190428", keyBuilder1.buildListKey("20190428"));
        assertEquals("message:20190428", keyBuilder2.buildListKey("20190428"));
    }

    @Test
    public void buildListKeys() {
        assertEquals(
            ImmutableList.of(
                "message:20190426",
                "message:20190425",
                "message:20190424",
                "message:20190423"
            ),
            keyBuilder1.buildListKeys());

        assertEquals(
            ImmutableList.of(
                "message:20190426",
                "message:20190425",
                "message:20190424",
                "message:20190423",
                "message:20190422",
                "message:20190421"
            ),
            keyBuilder2.buildListKeys());
    }

    @Test
    public void parseListKeyTest() {
        final var key1 = keyBuilder1.parseListKey("message:20190428");
        final var key2 = keyBuilder2.parseListKey("message:20190428");

        assertEquals("20190428", key1.getDt());
        assertNull(key1.getHash());
        assertEquals("20190428", key2.getDt());
        assertNull(key2.getHash());
    }
}
