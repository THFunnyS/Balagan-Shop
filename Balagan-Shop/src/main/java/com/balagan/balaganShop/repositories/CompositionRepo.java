package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.CompositionOfApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompositionRepo extends JpaRepository<CompositionOfApplication, Integer> {
}
