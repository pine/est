package moe.pine.est.log.repositories;


import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import moe.pine.est.log.models.MessageLog;
import moe.pine.est.log.models.MessageLogId;
import moe.pine.est.log.utils.MessageLogKeyBuilder;
import moe.pine.est.log.utils.TimeoutCalculator;
import moe.pine.est.murmur.Murmur3;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class MessageLogRepositoryTest extends TestBase {
    private static final int RETENTION_DAYS = 3;
    private static final int ILLEGAL_RETENTION_DAYS = -1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ObjectMapper objectMapper;

    @Mock
    private Murmur3 murmur3;

    @Mock
    private TimeoutCalculator timeoutCalculator;

    @Mock
    private MessageLogKeyBuilder messageLogKeyBuilder;

    private MessageLogRepository messageLogRepository;

    @Before
    public void setUp() {
        super.setUp();

        objectMapper = spy(new ObjectMapper());
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        messageLogRepository = new MessageLogRepository(
                redisTemplate,
                objectMapper,
                murmur3,
                timeoutCalculator,
                messageLogKeyBuilder,
                RETENTION_DAYS
        );
    }

    @Test
    public void constructorTest() {
        new MessageLogRepository(
                redisTemplate,
                objectMapper,
                murmur3,
                timeoutCalculator,
                messageLogKeyBuilder,
                RETENTION_DAYS
        );
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_nullRedisTemplate() {
        expectedException.expect(NullPointerException.class);

        new MessageLogRepository(
                null,
                objectMapper,
                murmur3,
                timeoutCalculator,
                messageLogKeyBuilder,
                RETENTION_DAYS
        );
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_nullObjectMapper() {
        expectedException.expect(NullPointerException.class);

        new MessageLogRepository(
                redisTemplate,
                null,
                murmur3,
                timeoutCalculator,
                messageLogKeyBuilder,
                RETENTION_DAYS
        );
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_nullMurmur3() {
        expectedException.expect(NullPointerException.class);

        new MessageLogRepository(
                redisTemplate,
                objectMapper,
                null,
                timeoutCalculator,
                messageLogKeyBuilder,
                RETENTION_DAYS
        );
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_nullTimeoutCalculator() {
        expectedException.expect(NullPointerException.class);

        new MessageLogRepository(
                redisTemplate,
                objectMapper,
                murmur3,
                null,
                messageLogKeyBuilder,
                RETENTION_DAYS
        );
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void constructorTest_nullKeyBuilder() {
        expectedException.expect(NullPointerException.class);

        new MessageLogRepository(
                redisTemplate,
                objectMapper,
                murmur3,
                timeoutCalculator,
                null,
                RETENTION_DAYS
        );
    }

    @Test
    public void constructorTest_illegalRetentionDays() {
        expectedException.expect(IllegalArgumentException.class);

        new MessageLogRepository(
                redisTemplate,
                objectMapper,
                murmur3,
                timeoutCalculator,
                messageLogKeyBuilder,
                ILLEGAL_RETENTION_DAYS
        );
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public void addTest() {
        final String dt = "20190502";
        final String hash1 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1";
        final String hash2 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx2";
        final String listKey = "message:" + dt;
        final String itemKey1 = "message:" + dt + ":" + hash1;
        final String itemKey2 = "message:" + dt + ":" + hash2;

        final var messageLog1 = new MessageLog();
        messageLog1.setFrom("example1@example.com");
        messageLog1.setBodyPlain("body");
        messageLog1.setTimestamp(123456L);

        final var messageLog2 = new MessageLog();
        messageLog2.setFrom("example2@example.com");
        messageLog2.setBodyHtml("body");
        messageLog2.setTimestamp(56789L);

        when(messageLogKeyBuilder.formattedDt()).thenReturn(dt);
        when(murmur3.hash128(anyString()))
                .thenReturn(hash1)
                .thenReturn(hash2);
        when(messageLogKeyBuilder.buildListKey(dt)).thenReturn(listKey);
        when(messageLogKeyBuilder.buildItemKey(dt, hash1)).thenReturn(itemKey1);
        when(messageLogKeyBuilder.buildItemKey(dt, hash2)).thenReturn(itemKey2);
        when(timeoutCalculator.calc(RETENTION_DAYS)).thenReturn(10L);

        final MessageLogId messageLogId1 = messageLogRepository.add(messageLog1);
        assertEquals(dt, messageLogId1.getDt());
        assertEquals(hash1, messageLogId1.getHash());

        assertTrue(redisTemplate.hasKey(listKey));
        assertTrue(redisTemplate.hasKey(itemKey1));
        assertEquals(
                Collections.singletonList(hash1),
                redisTemplate.opsForList().range(listKey, 0, 10));

        final MessageLogId messageLogId2 = messageLogRepository.add(messageLog2);
        assertEquals(dt, messageLogId2.getDt());
        assertEquals(hash2, messageLogId2.getHash());

        assertTrue(redisTemplate.hasKey(itemKey2));
        assertEquals(
                ImmutableList.of(hash1, hash2),
                redisTemplate.opsForList().range(listKey, 0, 10));

        final MessageLog savedMessageLog1 = messageLogRepository.get(messageLogId1);
        assertNull(savedMessageLog1.getSender());
        assertEquals("example1@example.com", savedMessageLog1.getFrom());
        assertEquals("body", savedMessageLog1.getBodyPlain());
        assertEquals(123456L, savedMessageLog1.getTimestamp());

        final MessageLog savedMessageLog2 = messageLogRepository.get(messageLogId2);
        assertNull(savedMessageLog2.getSender());
        assertEquals("example2@example.com", savedMessageLog2.getFrom());
        assertEquals("body", savedMessageLog2.getBodyHtml());
        assertEquals(56789L, savedMessageLog2.getTimestamp());
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public void addTest_nullMessageLog() {
        expectedException.expect(NullPointerException.class);
        messageLogRepository.add(null);
    }


}
