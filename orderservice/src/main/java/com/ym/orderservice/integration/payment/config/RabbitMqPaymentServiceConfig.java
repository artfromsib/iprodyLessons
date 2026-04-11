package com.ym.orderservice.integration.payment.config;

import com.ym.orderservice.integration.payment.config.properties.RabbitMqPaymentServiceProperties;
import com.ym.orderservice.integration.payment.dto.request.PayRequestDTO;
import com.ym.orderservice.integration.payment.dto.response.PayResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

import java.util.HashMap;
import java.util.Map;


@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RabbitMqPaymentServiceProperties.class)
public class RabbitMqPaymentServiceConfig {
    private final RabbitMqPaymentServiceProperties properties;
    @Bean
    public Queue payRequestQueue() {
        return QueueBuilder.durable(properties.queueRequestName())
                .build();
    }
    @Bean
    public DirectExchange payRequestExchange() {
        return new DirectExchange(properties.exchangeRequestName());
    }
    @Bean
    public Binding queueBinding(Queue payRequestQueue,
                                DirectExchange payRequestExchange) {
        return BindingBuilder
                .bind(payRequestQueue)
                .to(payRequestExchange)
                .with(properties.queueRequestName());
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
