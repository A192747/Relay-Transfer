package com.example.transfer.service;

import com.example.transfer.mapper.HashFileMapper;
import com.example.transfer.model.FileHash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkService {
    private final HashFileFieldRatioService hashFileFieldRatioService;
    private final HashService hashService;
    private final FileStorageService fileStorageService;
    private final HashFileMapper hashFileMapper;
    @Transactional
    public String save(MultipartFile file) {
        String fileHash = hashService.getHash();
        String fileName = file.getOriginalFilename();
        hashFileFieldRatioService.save(hashFileMapper.toEntity(fileName, fileHash));
        fileStorageService.save(fileHash, file);
        log.info("saved FileName = {}, Hash = {}", fileName, fileHash);
        return fileHash;
    }

    public File getFileByHash(String hash) {
        FileHash fileHash = hashFileFieldRatioService.getByHash(hash);
        if (fileHash != null) {
            fileStorageService.getFile(hash + '/' + fileHash.getFileName());
        }
        throw new NoSuchElementException("Не удалось найти файл!");
    }
}
