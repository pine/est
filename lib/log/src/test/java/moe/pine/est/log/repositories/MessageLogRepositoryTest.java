package moe.pine.est.log.repositories;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import moe.pine.est.log.models.MessageLog;
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

import static org.mockito.Mockito.when;


public class MessageLogRepositoryTest extends TestBase {
    private static final int RETENTION_DAYS = 3;
    private static final int ILLEGAL_RETENTION_DAYS = -1;
    private static final String DT = "20190502";
    private static final String HASH = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
    private static final String LIST_KEY = "message:" + DT;
    private static final String ITEM_KEY = "message:" + DT + ":" + HASH;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
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
    public void addTest() {
        final var messageLog = new MessageLog();

        when(messageLogKeyBuilder.formattedDt()).thenReturn(DT);
        when(objectMapper.writeValueAsString(messageLog)).thenReturn("{}");
        when(murmur3.hash128("{}")).thenReturn(HASH);
        when(messageLogKeyBuilder.buildListKey(DT)).thenReturn(LIST_KEY);
        when(messageLogKeyBuilder.buildItemKey(DT, HASH)).thenReturn(ITEM_KEY);
        when(timeoutCalculator.calc(RETENTION_DAYS)).thenReturn(10L);

        messageLogRepository.add(messageLog);


    }

    @Test
    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public void addTest_nullMessageLog() {
        expectedException.expect(NullPointerException.class);

        messageLogRepository.add(null);
    }


}
