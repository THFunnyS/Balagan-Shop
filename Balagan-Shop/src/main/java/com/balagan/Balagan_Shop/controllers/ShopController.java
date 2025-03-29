package com.balagan.Balagan_Shop.controllers;

import models.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import repositories.ApplicationRepo;

import java.util.Iterator;

@Controller
public class ShopController {
    @Autowired
    private ApplicationRepo applicationRepo;
    @GetMapping("/shop")
    public String shopMain(Model model){
        Iterable<Application> applications = applicationRepo.findAll();
        model.addAttribute("applications", applications);
        return "shop-main";
    }
}
