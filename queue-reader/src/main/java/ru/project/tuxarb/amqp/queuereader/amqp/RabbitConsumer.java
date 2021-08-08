package ru.project.tuxarb.amqp.queuereader.amqp;

import com.rabbitmq.client.Channel;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.project.tuxarb.amqp.queuereader.AppPropertiesContainer;
import ru.project.tuxarb.amqp.queuereader.core.ReceivedMessageOutputMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;

@Component
public class RabbitConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(RabbitConsumer.class);

    private static final String LINE_FEED = System.getProperty("line.separator");

    @Autowired
    private AppPropertiesContainer appPropertiesContainer;

    private File fileOutputModeResultDir;
    private String fileOutputModeResultFileExtension;

    @PostConstruct
    private void init() throws Exception {
        switch (ReceivedMessageOutputMode.from(appPropertiesContainer.getOutputMode())) {
            case FILE:
                LOG.info("applied a receive message output to FILE");
                String resultDirPath = appPropertiesContainer.getFileOutputModeResultDirPath();
                if (StringUtils.isEmpty(resultDirPath)) {
                    LOG.warn("an output file dir is not specified. Use default (relative the current dir): amqp-output");
                    this.fileOutputModeResultDir = new File("amqp-output");
                } else {
                    this.fileOutputModeResultDir = new File(resultDirPath);
                }
                Files.createDirectories(fileOutputModeResultDir.toPath());
                String resultFileExtension = appPropertiesContainer.getFileOutputModeResultFileExtension();
                if (StringUtils.isEmpty(resultFileExtension)) {
                    LOG.warn("an output file extension is not specified. Use default: .txt");
                    this.fileOutputModeResultFileExtension = ".txt";
                } else {
                    this.fileOutputModeResultFileExtension = resultFileExtension;
                }
                break;
            case CONSOLE:
                LOG.info("applied a receive message output to CONSOLE");
                break;
            default:
                LOG.warn("output for a received message is undefined!");
        }
    }

    @RabbitListener(queues = "#{appPropertiesContainer.amqpInboundQueue}", containerFactory = "rabbitListenerContainerFactory")
    public void receive(Message message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        String payload = new String(message.getBody(), StandardCharsets.UTF_8);
        Map<String, Object> messageHeaders = message.getMessageProperties().getHeaders();
        try {
            switch (ReceivedMessageOutputMode.from(appPropertiesContainer.getOutputMode())) {
                case FILE:
                    processReceiveMessageToFile(payload, messageHeaders);
                    break;
                case CONSOLE:
                default:
                    LOG.info("payload: {}, headers: {}", payload, messageHeaders);
                    break;
            }
        } catch (Exception e) {
            LOG.error("Error while receiving a message from amqp with payload: {}, headers: {}. Reason: {}", payload, messageHeaders, e);
        } finally {
            if (appPropertiesContainer.getNeedToAck()) {
                try {
                    channel.basicAck(tag, false);
                } catch (IOException e) {
                    LOG.error("Can't ack", e);
                }
            }
        }
    }

    private void processReceiveMessageToFile(String payload, Map<String, Object> messageHeaders) throws IOException {
        String outputFileName = UUID.randomUUID().toString() + fileOutputModeResultFileExtension;
        Path outputFilePath = Paths.get(fileOutputModeResultDir.getPath(), outputFileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath.toFile())) {
            IOUtils.write(payload, fileOutputStream, StandardCharsets.UTF_8);
            if (messageHeaders != null && !messageHeaders.isEmpty()) {
                IOUtils.write("###################### Headers ######################" + LINE_FEED, fileOutputStream, StandardCharsets.UTF_8);
                IOUtils.write(messageHeaders.toString(), fileOutputStream, StandardCharsets.UTF_8);
            }
        }
    }
}
