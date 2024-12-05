package com.example.runtime_config.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableAsync
public class RedisConfiguration {

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("localhost");
        configuration.setPort(6379);

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // Value serializer (can switch between String and JSON)
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
        RedisConnectionFactory connectionFactory,
        NimbleMessageListener listener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // listeners should do this themselves
        // listeners know what to do with the container, but the container should just "exist"
        // each bean wires themselves...
        container.addMessageListener(listener, listener.getChannelTopic());
        return container;
    }

    @Component
    public class RedisPublisher {
        private final RedisTemplate<String, String> redisTemplate;

        // Constructor-based injection is recommended over field injection
        public RedisPublisher(RedisTemplate<String, String> redisTemplate) {
            this.redisTemplate = redisTemplate;
        }

        public void publish(String channel, String message) {
            redisTemplate.convertAndSend(channel, message);
        }
    }

    public interface NimbleMessageListener extends MessageListener {
        ChannelTopic getChannelTopic();
    }

    // Generic Object Receiver
    @Getter
    public abstract class GenericMessageReceiver<T> implements NimbleMessageListener {
        private final Class<T> messageType;
        private ChannelTopic channelTopic;

        public GenericMessageReceiver(Class<T> messageType, ChannelTopic channelTopic) {
            this.messageType = messageType;
            this.channelTopic = channelTopic;
        }

        public void onMessage(Message message, byte[] pattern) {
            ObjectMapper om = new ObjectMapper();
            try {
                T payload = om.readValue(message.getBody(), messageType);
                processMessage(payload);
            } catch (Exception e) {
                System.out.println("failed");
            }
        }

        abstract void processMessage(T message);
    }

    // can we cascade refreshes?
    // refresh at config level vs bean refresh itself
    // problem: ordering of bean refresh, overcoming bean dependency

    // Example of a concrete implementation
    @Component
    public class ConfigurationChangeSubscriber extends GenericMessageReceiver<TestMessage> {

//        private static final String SUBSCRIBED_CHANNEL = "test-channel";

        public ConfigurationChangeSubscriber() {
        super(TestMessage.class, NimbleChannel.TEST_CHANNEL.getChannelTopic());
        }

        @Override
        protected void processMessage(TestMessage user) {
            System.out.println("Received value: " + user.getConfigValue());
        }
    }

}