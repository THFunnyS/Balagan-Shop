package com.balagan.balaganShop.controllers;

import com.balagan.balaganShop.models.Application;
import com.balagan.balaganShop.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/application")
public class ApplicationApiController {
    private final ApplicationService applicationService;

    public ApplicationApiController(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications(){
        return ResponseEntity.ok(applicationService.getAllApplications());
    }
}
