package com.example.transfer.service;

import com.example.transfer.entity.FileData;
import com.example.transfer.mapper.HashFileMapper;
import com.example.transfer.model.FileHash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkService {
    private final HashFileFieldRatioService hashFileFieldRatioService;
    private final HashService hashService;
    private final FileStorageService fileStorageService;
    private final HashFileMapper hashFileMapper;
    @Transactional
    public String save(MultipartFile file, Timestamp expiredAt) {
        String fileHash = hashService.getHash();
        String fileName = file.getOriginalFilename();


        expiredAt = checkExpired(expiredAt);

        hashFileFieldRatioService.save(hashFileMapper.toEntity(fileName, fileHash, expiredAt));
        fileStorageService.save(fileHash, file);
        log.info("saved FileName = {}, Hash = {}", fileName, fileHash);
        return fileHash;
    }

    public FileData getFileByHash(String hash) {
        FileHash fileHash = hashFileFieldRatioService.getByHash(hash);
        if (fileHash != null) {
            return fileStorageService.getFile(hash, fileHash.getFileName());
        }
        throw new NoSuchElementException("Не удалось найти файл!");
    }

    @Scheduled(fixedRateString = "${recheck-period:10}", timeUnit = TimeUnit.MINUTES)
    @Async
    public void checkAndDeleteIfExpired() {
        log.info("Searching expired files");
        List<FileHash> list = hashFileFieldRatioService.getAllExpired(new Timestamp(System.currentTimeMillis()));
        log.info("Found {} elements", list.size());
        list.forEach(this::deleteInServices);
    }

    @Transactional
    public void deleteInServices(FileHash elem) {
        String hash = elem.getHash();
        String name = elem.getFileName();
        fileStorageService.delete(hash, name);
        hashFileFieldRatioService.deleteByHash(hash);
    }

    //Установим время доступа к файлу по дефолту на 1 час


    public Timestamp checkExpired(Timestamp expiredAt) {
        if(expiredAt == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Timestamp(System.currentTimeMillis()));
            calendar.add(Calendar.HOUR, 1);
            return new Timestamp(calendar.getTimeInMillis());
        } else {
            if (expiredAt.before(new Timestamp(System.currentTimeMillis())))
                throw new IllegalArgumentException("Время доступа к файлу не может быть в прошлом");
        }
        return expiredAt;
    }
}
