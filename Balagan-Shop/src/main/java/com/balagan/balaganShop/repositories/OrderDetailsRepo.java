package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Order;
import com.balagan.balaganShop.models.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Integer> {
    Object findByOrder(Order order);
}
