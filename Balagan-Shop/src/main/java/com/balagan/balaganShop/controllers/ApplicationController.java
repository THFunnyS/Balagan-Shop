package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.*;
import com.balagan.balaganShop.repositories.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер для работы с заявками (Application),
 * корзиной, заказами и отображением данных на страницах.
 */
@Controller
public class ApplicationController {

    // Внедрение зависимостей репозиториев для доступа к данным
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

    /**
     * Отображение формы добавления заявки и списка доступных товаров.
     * Также загружаем данные из корзины пользователя (сессия).
     */
    @GetMapping("/application/add")
    public String applicationAdd(Model model, HttpSession session){
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        model.addAttribute("items", itemRepo.findAll());

        // Загружаем корзину из сессии, если есть
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        List<Item> selectedItems = itemRepo.findAllById(cart);
        model.addAttribute("selectedItems", selectedItems);

        return "application-add"; // возвращаем HTML-шаблон
    }

    /**
     * Отображение карточек заявок с товарами, постранично.
     */
    @GetMapping("/application/cards")
    public String showApplicationCards(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 6;
        Page<Item> itemPage = itemRepo.findAll(PageRequest.of(page, pageSize));
        List<Application> applications = applicationRepo.findAll();

        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", itemPage.getTotalPages());

        return "application-cards";
    }

    /**
     * Добавление товара в корзину (сохраняется в сессии пользователя).
     */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int itemId, HttpSession session) {
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        if (!cart.contains(itemId)) cart.add(itemId);
        session.setAttribute("cart", cart);
        return "redirect:/application/add";
    }

    /**
     * Удаление товара из корзины.
     */
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam int itemId, HttpSession session) {
        List<Integer> cart = (List<Integer>) session.getAttribute("cart");
        if (cart != null) cart.removeIf(id -> id == itemId);
        session.setAttribute("cart", cart);
        return "redirect:/application/add";
    }

    /**
     * Отправка заявки: создаётся Application, Order и OrderDetails.
     * Данные о корзине приходят в виде JSON (cartJson).
     */
    @PostMapping("/application/add")
    public String applicationPostAdd(@RequestParam String FIO,
                                     @RequestParam String phoneNumber,
                                     @RequestParam String telegram,
                                     @RequestParam String cartJson,
                                     Model model) {
        try {
            // Создание и сохранение заявки
            Application application = new Application(FIO, phoneNumber, telegram);
            applicationRepo.save(application);

            // Создание и сохранение заказа
            Order order = new Order();
            order.setApplication(application);
            order.setAmount_of_items(0);
            order.setValue_of_order(0.0);
            orderRepo.save(order);

            // Парсинг корзины из JSON
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> cart = mapper.readValue(cartJson, new TypeReference<>() {});

            int totalItems = 0;
            double totalValue = 0.0;

            // Добавляем детали заказа по каждому товару
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

            // Обновляем заказ
            order.setAmount_of_items(totalItems);
            order.setValue_of_order(totalValue);
            orderRepo.save(order);

            // Связываем заявку с заказом
            application.setOrder(order);
            applicationRepo.save(application);

            return "redirect:/application/cards";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при оформлении заявки");
            return "application-add";
        }
    }

    /**
     * Показ всех заявок
     */
    @GetMapping("/application/show")
    public String showApplications(Model model) {
        List<Application> applications = applicationRepo.findAll();
        List<Item> items = itemRepo.findAll();
        model.addAttribute("applications", applications);
        model.addAttribute("items", items);
        return "application-show";
    }

    /**
     * Удаление заявки по ID
     */
    @GetMapping("/application/delete/{id}")
    public String deleteApplication(@PathVariable("id") int id) {
        applicationRepo.deleteById(id);
        return "redirect:/application/show";
    }

    /**
     * Отображение формы редактирования заявки
     */
    @GetMapping("/application/edit/{id}")
    public String editItem(@PathVariable("id") int id, Model model){
        Application application = applicationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        List<Item> allItems = itemRepo.findAll();
        List<OrderDetails> orderDetails = orderDetailsRepo.findByOrder(application.getOrder());

        if (orderDetails == null){
            orderDetails = new ArrayList<>();
        }

        // Генерируем JSON, чтобы отобразить текущие позиции в заказе
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < orderDetails.size(); i++){
            OrderDetails orD = orderDetails.get(i);
            jsonBuilder.append("{")
                    .append("\"item\": {")
                    .append("\"id\": ").append(orD.getItem().getId()).append(",")
                    .append("\"name\": \"").append(orD.getItem().getName()).append("\",")
                    .append("\"value\": ").append(orD.getItem().getValue()).append(",")
                    .append("\"photo\": \"").append(orD.getItem().getPhoto()).append("\",")
                    .append("\"type\": \"").append(orD.getItem().getType().getType()).append("\",")
                    .append("\"size\": \"").append(orD.getItem().getSize().getSize()).append("\"")
                    .append("}")
                    .append("}");
            if (i != orderDetails.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");

        model.addAttribute("application", application);
        model.addAttribute("items", allItems);
        model.addAttribute("orderDetailsJson", jsonBuilder.toString());
        return "application-edit";
    }

    /**
     * Сохранение изменений в заявке после редактирования
     */
    @PostMapping("/application/edit/{id}")
    public String updateApplication(@PathVariable("id") int id,
                                    @RequestParam String FIO,
                                    @RequestParam String phoneNumber,
                                    @RequestParam String telegram,
                                    @RequestParam("cartJson") String cartJson) throws JsonProcessingException {

        Application application = applicationRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        // Обновление полей, если они не пустые
        if (FIO != null && !FIO.isBlank()) application.setFIO(FIO);
        if (phoneNumber != null && !phoneNumber.isBlank()) application.setPhoneNumber(phoneNumber);
        if (telegram != null && !telegram.isBlank()) application.setTelegram(telegram);

        Order order = application.getOrder();
        if (order == null){
            order = new Order();
            order.setApplication(application);
            orderRepo.save(order);
            application.setOrder(order);
        }

        // Получаем текущие детали заказа и группируем по itemId
        List<OrderDetails> existingDetails = orderDetailsRepo.findByOrder(order);
        Map<Integer, List<OrderDetails>> existingMap = new HashMap<>();
        for (OrderDetails detail : existingDetails) {
            int itemId = detail.getItem().getId();
            existingMap.computeIfAbsent(itemId, k -> new ArrayList<>()).add(detail);
        }

        // Парсинг нового списка товаров из JSON
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

        // Синхронизация старых и новых данных
        for (Map.Entry<Integer, Integer> entry : newCart.entrySet()) {
            int itemId = entry.getKey();
            int newCount = entry.getValue();

            List<OrderDetails> currentList = existingMap.getOrDefault(itemId, new ArrayList<>());
            int currentCount = currentList.size();

            if (newCount > currentCount) {
                // Добавляем недостающие
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
                // Удаляем лишние
                List<OrderDetails> toRemove = currentList.subList(0, currentCount - newCount);
                orderDetailsRepo.deleteAll(toRemove);
            }

            existingMap.remove(itemId); // исключаем обработанные
        }

        // Удаляем позиции, отсутствующие в новой корзине
        for (List<OrderDetails> toDelete : existingMap.values()) {
            orderDetailsRepo.deleteAll(toDelete);
        }

        // Пересчёт итоговой стоимости и количества
        List<OrderDetails> updatedDetails = orderDetailsRepo.findByOrder(order);
        int totalItems = updatedDetails.size();
        double totalValue = updatedDetails.stream().mapToDouble(d -> d.getItem().getValue()).sum();

        order.setAmount_of_items(totalItems);
        order.setValue_of_order(totalValue);
        orderRepo.save(order);
        applicationRepo.save(application);

        return "redirect:/application/show";
    }
}
