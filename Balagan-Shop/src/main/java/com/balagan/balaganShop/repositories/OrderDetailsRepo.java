package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Order;
import com.balagan.balaganShop.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Integer> {
    List<OrderDetails> findByOrder(Order order);
}
