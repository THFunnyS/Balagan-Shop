package com.balagan.balaganShop.config;

// Импорт аннотаций и классов, используемых для конфигурации
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Класс конфигурации, предназначенный для хранения настроек,
 * связанных с загрузкой файлов на сервер.
 *
 * Аннотация @Component делает этот класс Spring-бином,
 * чтобы его можно было внедрить в другие компоненты приложения.
 *
 * Аннотация @ConfigurationProperties указывает, что значения полей
 * будут автоматически загружены из application.properties или application.yml,
 * используя префикс "file".
 */
@Component
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    // Поле для хранения директории, куда будут загружаться файлы
    private String uploadDir;

    /**
     * Геттер для uploadDir.
     * @return путь к директории загрузки файлов
     */
    public String getUploadDir() {
        return uploadDir;
    }

    /**
     * Сеттер для uploadDir.
     * @param uploadDir путь, задаваемый из конфигурационного файла
     */
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
