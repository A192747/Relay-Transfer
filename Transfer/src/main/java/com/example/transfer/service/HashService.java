package com.example.transfer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class HashService {
    private final RestTemplate restTemplate;
    @Value("${services-urls.hash-generator}")
    private String HASH_SERVICE_URL;
    public String getHash() {
        if(HASH_SERVICE_URL != null && !HASH_SERVICE_URL.isBlank()) {
            return restTemplate.getForObject(HASH_SERVICE_URL, String.class);
        }
        throw new IllegalArgumentException("В настройках не указан адрес сервиса для получения хэша");
    }
}
