package com.epam.training.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.util.ErrorHandler;

@Slf4j
@Configuration
public class AppConfig {

    public static final int MAX_RETRY_COUNT = 3;
    public static final int REDELIVERY_DELAY_MILLIS = 2000;
    public static final int INITIAL_REDELIVERY_DELAY_MILLIS = 1000;
    public static final int BACK_OFF_MULTIPLIER = 2;
    public static final String TRAINING_TYPE_ID_PROPERTY_NAME = "training";

    @Value("${spring.artemis.broker-url}")
    private String brokerURL;
    @Value("${spring.artemis.user}")
    private String username;
    @Value("${spring.artemis.password}")
    private String password;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper());
        converter.setTypeIdPropertyName(TRAINING_TYPE_ID_PROPERTY_NAME);
        return converter;
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy policy = new RedeliveryPolicy();
        policy.setMaximumRedeliveries(MAX_RETRY_COUNT);
        policy.setInitialRedeliveryDelay(INITIAL_REDELIVERY_DELAY_MILLIS);
        policy.setRedeliveryDelay(REDELIVERY_DELAY_MILLIS);
        policy.setBackOffMultiplier(BACK_OFF_MULTIPLIER);
        policy.setUseExponentialBackOff(true);
        return policy;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username, password, brokerURL);
        factory.setRedeliveryPolicy(redeliveryPolicy());
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setMessageConverter(jacksonMessageConverter());
        factory.setConnectionFactory(connectionFactory);
        factory.setSessionTransacted(true);
        factory.setErrorHandler(jmsErrorHandler());
        return factory;
    }

    @Bean
    public ErrorHandler jmsErrorHandler() {
        return t -> log.error("JMS Error: {}", t.getMessage(), t);
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
}
