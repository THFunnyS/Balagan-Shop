package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepo extends JpaRepository<Item, Integer> {
}
