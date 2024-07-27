package com.example.hashgen.service;

import com.example.hashgen.util.HashGenerator;
import org.springframework.stereotype.Service;

@Service
public class HashService {
    public String getCache() {
        return HashGenerator.generate();
    }
}
