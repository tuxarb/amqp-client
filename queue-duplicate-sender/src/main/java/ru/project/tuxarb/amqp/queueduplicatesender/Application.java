package ru.project.tuxarb.amqp.queueduplicatesender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import ru.project.tuxarb.amqp.queueduplicatesender.amqp.RabbitSender;

import java.nio.file.Paths;

@SpringBootApplication(exclude = RabbitAutoConfiguration.class)
public class Application implements CommandLineRunner {

    @Autowired
    private RabbitSender rabbitSender;

    @Autowired
    private AppPropertiesContainer appPropertiesContainer;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        rabbitSender.readFileAndCreateDuplicateMessagesAndSendToAmqp(
                Paths.get(appPropertiesContainer.getFilePathToSendToAmqp()),
                appPropertiesContainer.getDuplicateMessagesCount(),
                appPropertiesContainer.getAmqpOutboundQueue()
        );
        System.exit(0);
    }
}