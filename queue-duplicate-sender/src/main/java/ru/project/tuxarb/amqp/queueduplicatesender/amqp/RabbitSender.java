package ru.project.tuxarb.amqp.queueduplicatesender.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

import static ru.project.tuxarb.amqp.common.util.FileUtil.readFileToString;

import static java.util.Objects.requireNonNull;

@Component
public class RabbitSender {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitSender.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void readFileAndCreateDuplicateMessagesAndSendToAmqp(Path filePath, int messageDuplicatesCount, String queueName) {
        requireNonNull(filePath);
        LOG.info("filePath={}, messageDuplicatesCount={}, queueName={}", filePath, messageDuplicatesCount, queueName);
        try {
            String fileContent = readFileToString(filePath);
            LOG.info("a file content to send to amqp: {}", fileContent);
            for (int i = 1; i <= messageDuplicatesCount; i++) {
                rabbitTemplate.convertAndSend(queueName, fileContent);
                if (i % 100 == 0) {
                    LOG.info("sent 100 messages to amqp...");
                }
            }
            LOG.info("all {} messages were successfully sent to amqp {}!", messageDuplicatesCount, queueName);
        } catch (Exception e) {
            LOG.error("a error is occurred while 'readFileAndCreateDuplicateMessagesAndSendToAmqp'", e);
        }
    }
}
