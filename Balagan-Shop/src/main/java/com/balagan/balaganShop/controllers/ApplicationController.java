package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.*;
import com.balagan.balaganShop.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private OrderRepo orderRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private SizeRepo sizeRepo;
    @Autowired
    private TypeRepo typeRepo;
    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

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
            // Создаем заявку
            Application application = new Application(FIO, phoneNumber, telegram);
            applicationRepo.save(application);

            // Создаем заказ
            Order order = new Order();
            order.setApplication(application);
            order.setAmount_of_items(0); // пока 0
            order.setValue_of_order(0.0); // пока 0
            orderRepo.save(order); // нужно сохранить, чтобы получить ID

            // Парсим корзину из JSON
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> cart = mapper.readValue(cartJson, new TypeReference<>() {});

            int totalItems = 0;
            double totalValue = 0.0;

            // Добавляем позиции в заявку
            for (Map<String, Object> itemData : cart) {
                int itemId = (int) itemData.get("id");
                int count = (int) itemData.get("count");

                Item item = itemRepo.findById(itemId).orElse(null);
                if (item == null) continue;

                for (int i = 0; i < count; i++) {
                    OrderDetails details = new OrderDetails();
                    details.setOrder(order);
                    details.setItem(item);
                    orderDetailsRepo.save(details);
                }

                totalItems += count;
                totalValue += item.getValue() * count;
            }

            // Обновляем заказ с финальными данными
            order.setAmount_of_items(totalItems);
            order.setValue_of_order(totalValue);
            orderRepo.save(order);

            // Привязываем заказ к заявке
            application.setOrder(order);
            applicationRepo.save(application);

            return "redirect:/application/add";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при оформлении заявки");
            return "application-add";
        }
    }
    //Вывод всех заявок
    @GetMapping("/application/show")
    public String showApplications(Model model) {
        List<Application> applications = applicationRepo.findAll();
        model.addAttribute("applications", applications);
        return "application-show";
    }

    //Удаление заявки
    @GetMapping("/application/delete/{id}")
    public String deleteApplication(@PathVariable("id") int id) {
        applicationRepo.deleteById(id);
        return "redirect:/application/show";
    }
    //Редактирование заявки
    @GetMapping("/application/edit/{id}")
    public String editItem(@PathVariable("id") int id, Model model){
        Application application = applicationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        List<Item> allItems = itemRepo.findAll();
        model.addAttribute("application", application);
        model.addAttribute("items", allItems);
        model.addAttribute("orderDetails", orderDetailsRepo.findByOrder(application.getOrder()));
        return "application-edit";
    }

    // POST для сохранения изменений заявки
    @PostMapping("/application/edit/{id}")
    public String updateApplication(@PathVariable("id") int id,
                                    @RequestParam String FIO,
                                    @RequestParam String phoneNumber,
                                    @RequestParam String telegram,
                                    @RequestParam("cartJson") String cartJson) throws JsonProcessingException {

        Application application = applicationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        // Обновляем только если пришли новые значения
        if (FIO != null && !FIO.isBlank()) application.setFIO(FIO);
        if (phoneNumber != null && !phoneNumber.isBlank()) application.setPhoneNumber(phoneNumber);
        if (telegram != null && !telegram.isBlank()) application.setTelegram(telegram);

        Order order = application.getOrder();
        if (order == null){
            order = new Order();
            order.setApplication(application);
            orderRepo.save(order); // Сохраняем сразу, чтобы получить ID для связей
            application.setOrder(order);
        }

        // Получаем текущие детали заказа
        List<OrderDetails> existingDetails = (List<OrderDetails>) orderDetailsRepo.findByOrder(order);
        Map<Integer, List<OrderDetails>> existingMap = new HashMap<>();
        for (OrderDetails detail : existingDetails) {
            int itemId = detail.getItem().getId();
            existingMap.computeIfAbsent(itemId, k -> new ArrayList<>()).add(detail);
        }

        // Парсим корзину
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> cart = mapper.readValue(cartJson, new TypeReference<>() {});

        Map<Integer, Integer> newCart = new HashMap<>();
        for (Map<String, Object> itemData : cart) {
            Integer itemId = (Integer) itemData.get("id");
            Integer count = (Integer) itemData.get("count");
            if (itemId != null && count != null && count > 0) {
                newCart.put(itemId, count);
            }
        }

        // Синхронизируем
        for (Map.Entry<Integer, Integer> entry : newCart.entrySet()) {
            int itemId = entry.getKey();
            int newCount = entry.getValue();

            List<OrderDetails> currentList = existingMap.getOrDefault(itemId, new ArrayList<>());
            int currentCount = currentList.size();

            if (newCount > currentCount) {
                // Добавить недостающее количество
                Item item = itemRepo.findById(itemId).orElse(null);
                if (item != null) {
                    for (int i = 0; i < newCount - currentCount; i++) {
                        OrderDetails newDetail = new OrderDetails();
                        newDetail.setOrder(order);
                        newDetail.setItem(item);
                        orderDetailsRepo.save(newDetail);
                    }
                }
            } else if (newCount < currentCount) {
                // Удалить лишние
                List<OrderDetails> toRemove = currentList.subList(0, currentCount - newCount);
                orderDetailsRepo.deleteAll(toRemove);
            }
            // Если количество одинаково — ничего не делаем
            existingMap.remove(itemId); // удаляем из карты, оставшиеся потом удалим
        }

        // Удаляем позиции, которых больше нет в новом списке
        for (List<OrderDetails> toDelete : existingMap.values()) {
            orderDetailsRepo.deleteAll(toDelete);
        }

        // Пересчёт итогов
        List<OrderDetails> updatedDetails = (List<OrderDetails>) orderDetailsRepo.findByOrder(order);
        int totalItems = updatedDetails.size();
        double totalValue = updatedDetails.stream().mapToDouble(d -> d.getItem().getValue()).sum();

        order.setAmount_of_items(totalItems);
        order.setValue_of_order(totalValue);
        orderRepo.save(order);
        applicationRepo.save(application);
        return "redirect:/application/show";
    }
}
