package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.Application;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ShopController {
    @Autowired
    private ApplicationRepo applicationRepo;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private SizeRepo sizeRepo;
    @Autowired
    private TypeRepo typeRepo;
    @GetMapping("/shop")
    public String shopMain(Model model){
        //Iterable<Application> applications = applicationRepo.findAll();
        //model.addAttribute("applications", applications);
        return "shop-main";
    }

    @GetMapping("/item/add")
    public String itemAdd(Model model){
        model.addAttribute("types", typeRepo.findAll());
        model.addAttribute("sizes", sizeRepo.findAll());
        return "item-add";
    }

    @PostMapping("/item/add")
    public String itemPostAdd(@RequestParam String name,
                              @RequestParam double value,
                              @RequestParam int type_id,
                              @RequestParam int size_id,
                              @RequestParam String photo,
                              Model model){
        Type type = typeRepo.findById(type_id)
                .orElseThrow(() -> new IllegalArgumentException("Тип не найден"));
        Size size = sizeRepo.findById(size_id)
                .orElseThrow(() -> new IllegalArgumentException("Размер не найден"));

        Item item = new Item(name, value, type, size, photo);
        itemRepo.save(item);
        return "redirect:/shop";
    }
}
