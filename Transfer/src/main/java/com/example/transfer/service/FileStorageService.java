package com.example.transfer.service;

import com.example.transfer.entity.FileData;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    boolean save(String path, MultipartFile file);

    boolean delete(String path, String fileName);

    FileData getFile(String path, String fileName);
}
