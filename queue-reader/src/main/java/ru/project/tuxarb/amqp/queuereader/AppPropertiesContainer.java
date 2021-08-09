package ru.project.tuxarb.amqp.queuereader;

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

    @Value("${amqp_inbound_queue}")
    private String amqpInboundQueue; // наименование очереди входящих сообщений
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
    @Value("${amqp_need_to_ack:true}")
    private Boolean needToAck;
    @Value("${amqp_consumer_fetch_messages_count}")
    private int consumerFetchMessagesCount;
    @Value("${output_mode:CONSOLE}") // что делать с полученным сообщением из очереди (по умолчанию пишем в консоль)
    private String outputMode;
    @Value("${file_output_mode_result_dir_path:null}") // путь к папке, куда сохранять сообщения как файлы для outputMode=FILE
    private String fileOutputModeResultDirPath;
    @Value("${file_output_mode_result_file_extension:null}") // расширение создаваемых файлов с содержимым сообщений для outputMode=FILE
    private String fileOutputModeResultFileExtension;

    @PostConstruct
    private void init() {
        LOG.info("initialized with the external properties: {}", JsonUtil.toJson(this));
    }
}
