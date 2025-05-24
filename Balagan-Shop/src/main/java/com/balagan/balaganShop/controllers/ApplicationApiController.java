package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.Application;
import com.balagan.balaganShop.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST-контроллер для обработки HTTP-запросов, связанных с сущностью Application.
 *
 * Аннотация @RestController говорит Spring, что этот класс является REST API контроллером.
 * Аннотация @RequestMapping задаёт базовый путь для всех эндпоинтов в этом контроллере.
 * В данном случае: /api/application
 */
@RestController
@RequestMapping("api/application")
public class ApplicationApiController {

    // Сервисный слой, содержащий бизнес-логику работы с Application
    private final ApplicationService applicationService;

    /**
     * Конструктор с внедрением зависимости через конструктор.
     * Spring автоматически подставит реализацию ApplicationService.
     */
    public ApplicationApiController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    /**
     * Обрабатывает GET-запросы по адресу /api/application
     *
     * Возвращает список всех объектов Application, обёрнутых в ResponseEntity с HTTP-статусом 200 OK.
     * Это может быть, например, список заявок, отправленных пользователями.
     *
     * @return ResponseEntity с телом в виде списка объектов Application
     */
    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(){
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
}
