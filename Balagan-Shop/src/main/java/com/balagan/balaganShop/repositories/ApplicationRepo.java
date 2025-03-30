package com.balagan.balaganShop.repositories;

import com.balagan.balaganShop.models.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepo extends JpaRepository<Application, Integer> {

}
