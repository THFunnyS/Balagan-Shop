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

    @Autowired
    public ItemController(FileStorageProperties fileStorageProperties) {
        this.uploadPath = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.uploadPath);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для загрузок: " + this.uploadPath, e);
        }
    }

    @GetMapping("/shop")
    public String shopMain(Model model){
        return "shop-main";
    }

    @GetMapping("/item/add")
    public String itemAdd(Model model){
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("items", itemRepo.findAll());
        return "item-add";
    }

    // Метод добавления товара с загрузкой фото
    @PostMapping("/item/add")
    public String itemPostAdd(@RequestParam String name,
                              @RequestParam double value,
                              @RequestParam int type_id,
                              @RequestParam int size_id,
                              @RequestParam("photo") MultipartFile photoFile,
                              Model model) {

        Type type = typeRepo.findById(type_id)
                .orElseThrow(() -> new IllegalArgumentException("Тип не найден"));
        Size size = sizeRepo.findById(size_id)
                .orElseThrow(() -> new IllegalArgumentException("Размер не найден"));

        String filename = null;
        if (!photoFile.isEmpty()) {
            try {
                // Здесь папка уже гарантированно существует
                filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                photoFile.transferTo(filePath.toFile());
            } catch (IOException e) {
                e.printStackTrace();
                // Можно добавить обработку ошибок
            }
        }

        Item item = new Item(name, value, type, size, filename);
        itemRepo.save(item);
        return "redirect:/item/add";
    }

    @GetMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable("id") int id) {
        itemRepo.deleteById(id);
        return "redirect:/item/add";
    }

    @GetMapping("/item/edit/{id}")
    public String editItem(@PathVariable("id") int id, Model model){
        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        model.addAttribute("item", item);
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        return "item-edit";
    }

    // Метод сохранения изменений товара с загрузкой фото
    @PostMapping("/item/edit/{id}")
    public String saveEditedItem(@PathVariable("id") int id,
                                 @RequestParam String name,
                                 @RequestParam double value,
                                 @RequestParam int type_id,
                                 @RequestParam int size_id,
                                 @RequestParam("photo") MultipartFile photoFile) {

        Item item = itemRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден"));

        Type type = typeRepo.findById(type_id)
                .orElseThrow(() -> new IllegalArgumentException("Тип не найден"));
        Size size = sizeRepo.findById(size_id)
                .orElseThrow(() -> new IllegalArgumentException("Размер не найден"));

        item.setName(name);
        item.setValue(value);
        item.setType(type);
        item.setSize(size);

        if (!photoFile.isEmpty()) {
            try {
                String filename = System.currentTimeMillis() + "_" + photoFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(filename);
                photoFile.transferTo(filePath.toFile());
                item.setPhoto(filename);
            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибки
            }
        }

        itemRepo.save(item);
        return "redirect:/item/add";
    }
}

