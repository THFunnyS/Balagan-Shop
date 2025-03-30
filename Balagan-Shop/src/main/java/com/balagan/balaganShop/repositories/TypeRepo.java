package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepo extends JpaRepository<Type, Integer> {
}
