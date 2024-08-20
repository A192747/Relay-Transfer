package com.example.transfer.service;

import com.example.transfer.entity.FileData;
import com.example.transfer.mapper.HashFileMapper;
import com.example.transfer.model.FileHash;
import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${expired-at.amount:5}")
    private Integer amount;

    @Transactional
    public String save(MultipartFile file, Timestamp expiredAt) {
        String fileHash = hashService.getHash();
        String fileName = file.getOriginalFilename();

        log.info(String.valueOf(new Timestamp(System.currentTimeMillis())));

        expiredAt = checkAndFormExpiredAtTime(expiredAt);

        log.info(String.valueOf(expiredAt));

        hashFileFieldRatioService.save(hashFileMapper.toEntity(fileName, fileHash, expiredAt));
        fileStorageService.save(fileHash, file);
        log.info("saved FileName = {}, Hash = {}", fileName, fileHash);
        return fileHash;
    }
    @Cacheable(cacheNames = "file", key = "#hash")//, sync = true)
    public FileData getFileByHash(String hash) {
        log.info("download file {} at time {}", hash, Calendar.getInstance().getTime());
        FileHash fileHash = hashFileFieldRatioService.getByHash(hash);
        if (fileHash != null) {
            return fileStorageService.getFile(hash, fileHash.getFileName());
        }
        throw new NoSuchElementException("Не удалось найти файл!");
    }

    @Scheduled(fixedRateString = "${recheck-period:1}", timeUnit = TimeUnit.MINUTES)
    @Async
    public void checkAndDeleteIfExpired() {
        log.info("Searching expired files");
        List<FileHash> list = hashFileFieldRatioService.getAllExpired(new Timestamp(System.currentTimeMillis()));
        log.info("Found {} elements", list.size());
        list.forEach(this::deleteInServices);
    }

    @Transactional
    //почему-то не удаляются значения из кэша...
    @CacheEvict(cacheNames = "file", key = "#elem.hash")
    public void deleteInServices(FileHash elem) {
        String hash = elem.getHash();
        String name = elem.getFileName();
        if(fileStorageService.delete(hash, name))
            hashFileFieldRatioService.deleteById(elem.getId());
        else
            throw new InternalException("Не удалось удалить файл");
    }


    private Timestamp checkAndFormExpiredAtTime(Timestamp expiredAt) {
        if(expiredAt == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Timestamp(System.currentTimeMillis()));
            calendar.add(Calendar.MINUTE, amount);
            return new Timestamp(calendar.getTimeInMillis());
        } else {
            if (expiredAt.before(new Timestamp(System.currentTimeMillis())))
                throw new IllegalArgumentException("Время доступа к файлу не может быть в прошлом");
        }
        return expiredAt;
    }

}
