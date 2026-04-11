package com.ym.paymentservice.integration.order.config;

import com.ym.paymentservice.integration.order.config.properties.RabbitMqOrderServiceProperties;
import com.ym.paymentservice.integration.order.dto.request.PayRequestDTO;
import com.ym.paymentservice.integration.order.dto.response.PayResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class RabbitMqOrderServiceConfig {

    private final RabbitMqOrderServiceProperties properties;

    @Bean
    public Queue payResponseQueue() {
        return QueueBuilder.durable(properties.queueResponseName())
                .build();
    }

    @Bean
    public DirectExchange payResponseExchange() {
        return new DirectExchange(properties.exchangeResponseName());
    }

    @Bean
    public Binding queueBinding(Queue payResponseQueue,
                                DirectExchange payResponseExchange) {
        return BindingBuilder
                .bind(payResponseQueue)
                .to(payResponseExchange)
                .with(properties.queueResponseName());
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonConverter());
        factory.setDefaultRequeueRejected(false);
        factory.setAutoStartup(true);
        return factory;
    }
    @Bean
    public JacksonJsonMessageConverter jsonConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();
        converter.setClassMapper(classMapper());
        converter.getJavaTypeMapper().addTrustedPackages("com.ym", "java");
        return converter;
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();

        idClassMapping.put("pay-request", PayRequestDTO.class);
        idClassMapping.put("pay-response", PayResponseDTO.class);

        classMapper.setIdClassMapping(idClassMapping);
        return classMapper;
    }


}
