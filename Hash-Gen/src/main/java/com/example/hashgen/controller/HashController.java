package com.example.hashgen.controller;

import com.example.hashgen.service.HashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class HashController {

    private final HashService service;

    @GetMapping("/cache")
    public String getCache() {
        return service.getCache();
    }
}
