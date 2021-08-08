package ru.project.tuxarb.amqp.queueduplicatesender;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.project.tuxarb.amqp.common.util.JsonUtil;

import javax.annotation.PostConstruct;

/**
 * Spring-бин для хранения значений всех внешних настроек из application.properties.
 */
@Getter
@Setter
@Component
public class AppPropertiesContainer {
    private static final Logger LOG = LoggerFactory.getLogger(AppPropertiesContainer.class);

    @Value("${amqp_outbound_queue}")
    private String amqpOutboundQueue; // наименование очереди, куда отправлять сообщения
    @Value("${amqp_host}")
    private String amqpHost;
    @Value("${amqp_virtual_host:/}")
    private String amqpVirtualHost;
    @Value("${amqp_port}")
    private int amqpPort;
    @Value("${amqp_username}")
    private String amqpUsername;
    @Value("${amqp_password}")
    private String amqpPassword;
    @Value("${file_path_to_send}")
    private String filePathToSendToAmqp; // путь к файлу, контент которого необходимо продублировать и отправить в очередь AMQP
    @Value("${duplicate_messages_count:1}")
    private int duplicateMessagesCount; // число сообщений, которые будут отправлены в очередь AMQP. Каждое сообщение будет иметь один и тот же payload

    @PostConstruct
    private void init() {
        LOG.info("initialized with the external properties: {}", JsonUtil.toJson(this));
    }
}
