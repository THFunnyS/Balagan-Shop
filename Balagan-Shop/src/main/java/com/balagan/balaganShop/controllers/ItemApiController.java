package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.Item;
import com.balagan.balaganShop.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemApiController {
    private final ItemService itemService;

    public ItemApiController(ItemService itemService){
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems(){
        return ResponseEntity.ok(itemService.getAllItems());
    }
}
