package com.balagan.balaganShop.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Класс конфигурации Spring MVC.
 * Используется для настройки дополнительных обработчиков ресурсов.
 *
 * Аннотация @Configuration указывает, что это класс конфигурации Spring.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Переопределяем метод для добавления пользовательских обработчиков ресурсов.
     * В данном случае настраиваем отображение статических файлов (например, изображений),
     * загруженных пользователем и сохранённых в локальной папке "uploads".
     *
     * Пример: если пользователь переходит по адресу http://localhost:8080/uploads/image.jpg,
     * то Spring будет искать файл по пути file:uploads/image.jpg на файловой системе сервера.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Указываем, что все запросы, начинающиеся с "/uploads/**",
        // должны обслуживаться как статические ресурсы из папки "uploads/" на диске.
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
