package com.balagan.balaganShop.service;

import com.balagan.balaganShop.models.Item;
import com.balagan.balaganShop.repositories.ItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepo itemRepo;

    public ItemService(ItemRepo itemRepo){
        this.itemRepo = itemRepo;
    }

    public List<Item> getAllItems(){
        return itemRepo.findAll();
    }
}
