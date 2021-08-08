package ru.project.tuxarb.amqp.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс с утилитами для работы с JSON.
 */
public final class JsonUtil {

    private static final Logger LOG = LoggerFactory.getLogger(JsonUtil.class);

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private JsonUtil() {
    }

    /**
     * Метод для преобразования объекта в JSON-строку.
     *
     * @param object объект
     * @return json-строка или {@code null}, если переданный объект {@code null} или возникла ошибка
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOG.warn("Can't convert an object to a json string", e);
            return null;
        }
    }
}
