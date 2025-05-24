package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.Item;
import com.balagan.balaganShop.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // Указывает, что класс является REST-контроллером, и возвращает данные напрямую в теле ответа
@RequestMapping("/api/item") // Все запросы, начинающиеся с /api/item, обрабатываются этим контроллером
public class ItemApiController {

    private final ItemService itemService;

    // Конструктор с внедрением зависимости через параметр — сервис для работы с товарами
    public ItemApiController(ItemService itemService){
        this.itemService = itemService;
    }

    /**
     * Обработка GET-запроса по адресу /api/item
     * Возвращает список всех товаров в формате JSON
     *
     * @return HTTP 200 OK и список объектов Item
     */
    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(){
        // Получаем список товаров через сервис и возвращаем его в ответе
        return ResponseEntity.ok(itemService.getAllItems());
    }
}
