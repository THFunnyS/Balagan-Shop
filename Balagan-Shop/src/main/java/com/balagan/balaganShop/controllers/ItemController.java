package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.config.FileStorageProperties;
import com.balagan.balaganShop.models.Item;
import com.balagan.balaganShop.models.Size;
import com.balagan.balaganShop.models.Type;
import com.balagan.balaganShop.repositories.ApplicationRepo;
import com.balagan.balaganShop.repositories.ItemRepo;
import com.balagan.balaganShop.repositories.SizeRepo;
import com.balagan.balaganShop.repositories.TypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ItemController {
    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private SizeRepo sizeRepo;
    @Autowired
    private TypeRepo typeRepo;

    private final Path uploadPath;

    // Конструктор контроллера. Создаёт директорию для загрузки файлов, если она не существует.
    @Autowired
    public ItemController(FileStorageProperties fileStorageProperties) {
        this.uploadPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadPath); // создание директории для загрузки файлов
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для загрузок: " + this.uploadPath, e);
        }
    }

    // Отображение основной страницы магазина
    @GetMapping("/shop")
    public String shopMain(Model model){
        return "shop-main";
    }

    // Страница добавления нового товара
    @GetMapping("/item/add")
    public String itemAdd(Model model){
        model.addAttribute("types", typeRepo.findAll()); // список всех типов для выпадающего списка
        model.addAttribute("sizes", sizeRepo.findAll()); // список всех размеров
        model.addAttribute("items", itemRepo.findAll()); // список уже существующих товаров
        return "item-add";
    }

    // Обработка POST-запроса на добавление нового товара
    @PostMapping("/item/add")
    public String itemPostAdd(@RequestParam String name,
                              @RequestParam double value,
                              @RequestParam int type_id,
                              @RequestParam int size_id,
                              @RequestParam("photo") MultipartFile photoFile,
                              Model model) {

        // Получаем объекты типа и размера по их ID
        Type type = typeRepo.findById(type_id)
                .orElseThrow(() -> new IllegalArgumentException("Тип не найден"));
        Size size = sizeRepo.findById(size_id)
                .orElseThrow(() -> new IllegalArgumentException("Размер не найден"));

        String filename = null;
        if (!photoFile.isEmpty()) {
            try {
                // Генерация уникального имени файла и сохранение его на диск
                filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                photoFile.transferTo(filePath.toFile());
            } catch (IOException e) {
                e.printStackTrace(); // логируем ошибку
                // можно дополнительно обработать ошибку, например, показать сообщение пользователю
            }
        }

        // Создание и сохранение нового товара
        Item item = new Item(name, value, type, size, filename);
        itemRepo.save(item);
        return "redirect:/item/add"; // перенаправление обратно на страницу добавления товара
    }

    // Удаление товара по ID
    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemRepo.deleteById(id);
        return "redirect:/item/add";
    }

    // Отображение страницы редактирования товара
    @GetMapping("/item/edit/{id}")
    public String editItem(@PathVariable("id") int id, Model model){
        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        model.addAttribute("item", item); // передаём редактируемый товар
        model.addAttribute("types", typeRepo.findAll()); // список всех типов
        model.addAttribute("sizes", sizeRepo.findAll()); // список всех размеров
        return "item-edit";
    }

    // Обработка POST-запроса на сохранение изменений товара
    @PostMapping("/item/edit/{id}")
    public String saveEditedItem(@PathVariable("id") int id,
                                 @RequestParam String name,
                                 @RequestParam double value,
                                 @RequestParam int type_id,
                                 @RequestParam int size_id,
                                 @RequestParam("photo") MultipartFile photoFile) {

        // Поиск существующего товара
        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        // Получение типа и размера по ID
        Type type = typeRepo.findById(type_id)
                .orElseThrow(() -> new IllegalArgumentException("Тип не найден"));
        Size size = sizeRepo.findById(size_id)
                .orElseThrow(() -> new IllegalArgumentException("Размер не найден"));

        // Обновление полей товара
        item.setName(name);
        item.setValue(value);
        item.setType(type);
        item.setSize(size);

        // Если загружено новое фото — сохраняем и заменяем
        if (!photoFile.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                photoFile.transferTo(filePath.toFile());
                item.setPhoto(filename);
            } catch (IOException e) {
                e.printStackTrace(); // логируем ошибку
                // можно добавить уведомление пользователю
            }
        }

        // Сохраняем обновлённый товар
        itemRepo.save(item);
        return "redirect:/item/add";
    }
}
