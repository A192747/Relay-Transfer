package com.example.transfer.service;


import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    @Value("${minio.bucket-name}")
    private String BUCKET_NAME;


    @SneakyThrows
    @Override
    public boolean save(String path, MultipartFile file) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .contentType(file.getContentType())
                    .object(path + '/' + file.getOriginalFilename())
                    .stream(file.getInputStream(), -1, 10485760)
                    .build()
            );
            log.info("file {} saved", file.getOriginalFilename());
            return true;
        } catch (IOException | MinioException ex) {
            throw new IllegalStateException(ex.getMessage());
        }
    }

    @Override
    public boolean delete(String fileName) {
        try {
            Path path = Path.of(fileName);

            minioClient.deleteObjectTags(DeleteObjectTagsArgs.builder()
                        .bucket(BUCKET_NAME)
                        .object(fileName)
                        .build());
            log.info("file {} removed ", fileName);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            return false;
        }
        return true;
    }

    @Override
    @SneakyThrows
    public File getFile(String fileName) {

        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(fileName)
                .build());
        byte[] fileBytes = response.readAllBytes();
        File tempFile = File.createTempFile("downloaded-", ".tmp");

        // Записываем байты в файл
        Files.write(Paths.get(tempFile.getPath()), fileBytes);
        return tempFile;
    }
}
