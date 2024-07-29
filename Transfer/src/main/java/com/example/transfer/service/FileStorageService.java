package com.example.transfer.service;

import com.example.transfer.payload.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {
    FileResponse save(Path path, MultipartFile file);
    boolean delete(String fileMetadata);
    FileResponse getFile(String fileName);
}
