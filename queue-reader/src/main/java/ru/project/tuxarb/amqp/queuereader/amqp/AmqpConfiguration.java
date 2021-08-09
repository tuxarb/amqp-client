package ru.project.tuxarb.amqp.queuereader.amqp;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.project.tuxarb.amqp.queuereader.AppPropertiesContainer;

@EnableRabbit
@Configuration
public class AmqpConfiguration {

    @Autowired
    private AppPropertiesContainer appPropertiesContainer;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(
                appPropertiesContainer.getAmqpHost(), appPropertiesContainer.getAmqpPort()
        );
        factory.setUsername(appPropertiesContainer.getAmqpUsername());
        factory.setPassword(appPropertiesContainer.getAmqpPassword());
        factory.setVirtualHost(appPropertiesContainer.getAmqpVirtualHost());
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(appPropertiesContainer.getConsumerFetchMessagesCount());
        return factory;
    }
}
