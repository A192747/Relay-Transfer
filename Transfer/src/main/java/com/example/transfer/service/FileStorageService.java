package com.example.transfer.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileStorageService {
    boolean save(String path, MultipartFile file);

    boolean delete(String fileMetadata);

    File getFile(String fileName);
}
