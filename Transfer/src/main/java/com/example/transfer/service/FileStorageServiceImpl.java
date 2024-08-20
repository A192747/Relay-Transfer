package com.example.transfer.service;


import com.example.transfer.entity.FileData;
import com.example.transfer.model.FileHash;
import io.minio.*;
import io.minio.errors.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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
    public boolean delete(String path, String fileName) {
        if(deleteFile(path + '/' + fileName)) {
            return deleteFile(path);
        }
        return false;
    }

    @Override
    @SneakyThrows
    public FileData getFile(String path, String fileName) {
        GetObjectResponse response = minioClient.getObject(GetObjectArgs.builder()
                .bucket(BUCKET_NAME)
                .object(path + '/' + fileName)
                .build());

        return new FileData(response.readAllBytes(), fileName);
    }


    private boolean deleteFile(String path) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(BUCKET_NAME)
                    .object(path)
                    .build());
            log.info("file {} deleted", path);
        } catch (ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException |
                 InvalidResponseException | IOException | NoSuchAlgorithmException | ServerException |
                 XmlParserException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

}
