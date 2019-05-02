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
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


public class MessageLogRepositoryTest {
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

    private RedisTemplate<String, String> redisTemplate;

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
        final var configuration = new RedisStandaloneConfiguration("localhost", 6379);
        configuration.setDatabase(1);

        final var factory = new LettuceConnectionFactory(configuration);
        factory.afterPropertiesSet();

        redisTemplate = spy(new RedisTemplate<>());
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

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
