package ru.project.tuxarb.amqp.queueduplicatesender.amqp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.project.tuxarb.amqp.queueduplicatesender.AppPropertiesContainer;

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
    public AmqpTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }
}
