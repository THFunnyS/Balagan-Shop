package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepo extends JpaRepository<Size, Integer> {
}
