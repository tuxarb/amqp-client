package ru.project.tuxarb.amqp.common.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.project.tuxarb.amqp.common.util.exception.FileNotFoundException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Класс с утилитами для работы с файлами.
 */
public final class FileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {
    }

    public static void checkFileExists(Path filePath) {
        requireNonNull(filePath);
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(format("a file %s is not found", filePath));
        }
    }

    public static String readFileToString(Path filePath) throws IOException {
        checkFileExists(filePath);
        return FileUtils.readFileToString(filePath.toFile(), StandardCharsets.UTF_8);
    }
}
