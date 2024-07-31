package com.example.hashgen.service;


import com.example.hashgen.util.HashGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashService {
    public String getHash() {
        return HashGenerator.generate();
    }
}
