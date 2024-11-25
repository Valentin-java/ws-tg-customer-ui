package com.workers.config.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.workers.business.common.context.GenericContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, GenericContext> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, GenericContext> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Настройка сериализатора для ключей
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Настройка ObjectMapper с использованием PolymorphicTypeValidator
        ObjectMapper objectMapper = new ObjectMapper();

        // Создаем PolymorphicTypeValidator для ограничения десериализации
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.workers.business.common.context")
                .build();

        // Активируем типизацию с использованием PolymorphicTypeValidator
        objectMapper.activateDefaultTyping(
                ptv,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // Используем GenericJackson2JsonRedisSerializer с настроенным ObjectMapper
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // Устанавливаем сериализаторы для значений
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
