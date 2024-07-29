package com.example.transfer.service;

import com.example.transfer.mapper.FileResponseMapper;
import com.example.transfer.payload.FileResponse;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.ObjectStat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private MinioService minioService;
    private FileResponseMapper fileResponseMapper;
    @Override
    public FileResponse save(Path path, MultipartFile file) {
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
            ObjectStat metadata = minioService.getMetadata(path);
            log.info("this file {} storage in bucket: {} on date: {}", metadata.name(), metadata.bucketName(), metadata.createdTime());
            return fileResponseMapper.fileAddResponse(metadata);
        } catch (IOException | MinioException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(String fileName) {
        try {
            Path path = Path.of(fileName);
            ObjectStat metadata = minioService.getMetadata(path);
            minioService.remove(path);
            log.info("this file {} removed in bucket: {} on date: {}", metadata.name(), metadata.bucketName(), metadata.createdTime());
        } catch (MinioException e) {
            return false;
        }
        return true;
    }

    @Override
    public FileResponse getFile(String fileName) {
        return null;
    }
}
