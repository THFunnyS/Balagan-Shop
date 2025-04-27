package com.balagan.balaganShop.service;

import com.balagan.balaganShop.models.Application;
import com.balagan.balaganShop.repositories.ApplicationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepo applicationRepo;

    public ApplicationService(ApplicationRepo applicationRepo){
        this.applicationRepo = applicationRepo;
    }

    public List<Application> getAllApplications() {
        return applicationRepo.findAll();
    }
}
