package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.Application;
import com.balagan.balaganShop.models.CompositionOfApplication;
import com.balagan.balaganShop.models.Item;
import com.balagan.balaganShop.repositories.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class ApplicationController {
    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private CompositionRepo compositionRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private SizeRepo sizeRepo;
    @Autowired
    private TypeRepo typeRepo;

    // Показываем форму заявки и список товаров
    @GetMapping("/application/add")
    public String applicationAdd(Model model, HttpSession session){
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("items", itemRepo.findAll());

        // Корзина из сессии
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        List<Item> selectedItems = (List<Item>) itemRepo.findAllById(cart);
        model.addAttribute("selectedItems", selectedItems);

        return "application-add";
    }

    // Добавить товар в корзину
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int itemId, HttpSession session) {
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        if (!cart.contains(itemId)) cart.add(itemId);
        session.setAttribute("cart", cart);
        return "redirect:/application/add";
    }

    // Удалить товар из корзины
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam int itemId, HttpSession session) {
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart != null) cart.removeIf(id -> id == itemId);
        session.setAttribute("cart", cart);
        return "redirect:/application/add";
    }

    // Отправка заявки
    @PostMapping("/application/add")
    public String applicationPostAdd(@RequestParam String FIO,
                                     @RequestParam String phoneNumber,
                                     @RequestParam String telegram,
                                     @RequestParam String cartJson,
                                     Model model) {
        try {
            // 1. Создаем заявку
            Application application = new Application(FIO, phoneNumber, telegram);
            applicationRepo.save(application);

            // 2. Парсим корзину из JSON
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> cart = mapper.readValue(cartJson, new TypeReference<List<Map<String, Object>>>() {
            });

            // 3. Добавляем позиции в заявку
            for (Map<String, Object> itemData : cart) {
                int itemId = (int) itemData.get("id");
                int count = (int) itemData.get("count");

                Item item = itemRepo.findById(itemId).orElse(null);
                if (item == null) continue;

                CompositionOfApplication comp = new CompositionOfApplication();
                comp.setApplication(application);
                comp.setItems(item);
                comp.setAmount_of_items(count);
                comp.setValue_of_composition(item.getValue() * count);

                compositionRepo.save(comp);
            }

            return "redirect:/shop";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при оформлении заявки");
            return "application-add";
        }
    }
}
