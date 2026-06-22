package com.store.payment_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.StatelessRetryOperationsInterceptor;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.host}")
    private String hostName;

    @Value("${rabbitmq.username}")
    private String userName;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.port}")
    private int port;

    @Value("${rabbitmq.orderqueue}")
    private String orderQueue;

    @Value("${rabbitmq.virtual-host}")
    private String vHost;

    @Bean
    public CachingConnectionFactory connectionFactory () throws Exception {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(hostName);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setPort(port);
        connectionFactory.setVirtualHost(vHost);
        return connectionFactory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory listenerContainerFactory (
            ConnectionFactory connectionFactory, StatelessRetryOperationsInterceptor retryOperationsInterceptor,
            MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAdviceChain(retryOperationsInterceptor);
        factory.setMessageConverter(messageConverter);
        return factory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() throws Exception {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    Queue createQueue() throws Exception {
        Queue q = QueueBuilder.durable(orderQueue).build();
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    Queue boqQueue() throws Exception {
        Queue q = QueueBuilder.durable("boq." + orderQueue).build();
        amqpAdmin().declareQueue(q);
        return q;
    }

    @Bean
    public RepublishMessageRecoverer messageRecoverer (RabbitTemplate rabbitTemplate) {
        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(rabbitTemplate);
        recoverer.setErrorRoutingKeyPrefix("boq.");
        return recoverer;
    }

    @Bean
    public StatelessRetryOperationsInterceptor retryOperationsInterceptor (
            RepublishMessageRecoverer recoverer
    ) {
        StatelessRetryOperationsInterceptor interceptor = RetryInterceptorBuilder
                .stateless()
                .maxRetries(2)
                .backOffOptions(2000, 1, 100000)
                .recoverer(recoverer)
                .build();
        return interceptor;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
