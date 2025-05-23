package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Integer> {
    Manager findByLogin(String login);
}
