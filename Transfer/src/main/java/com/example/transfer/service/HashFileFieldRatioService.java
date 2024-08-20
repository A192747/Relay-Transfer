package com.example.transfer.service;

import com.example.transfer.model.FileHash;
import com.example.transfer.repository.HashFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class HashFileFieldRatioService {
    private final HashFileRepository hashFileRepository;
    public FileHash save(FileHash fileHash) {
        return hashFileRepository.save(fileHash);
    }

    public FileHash getByHash(String hash) {
        return hashFileRepository.findFileHashByHash(hash);
    }

    public void deleteById(UUID id) {
        hashFileRepository.deleteFileHashById(id);
    }

    public List<FileHash> getAllExpired(Timestamp currentTime) {
        List<FileHash> result = new ArrayList<>();
        int pageNumber = 0;
        Slice<FileHash> page = hashFileRepository.findAll(PageRequest.of(pageNumber, 10));;

        while (true) {
            log.info(page.getContent().toString());
            result.addAll(getExpiredOnPage(page, currentTime));
            pageNumber++;
            if(page.hasNext())
                page = hashFileRepository.findAll(PageRequest.of(pageNumber, 10));
            else
                break;
        }
        return result;
    }

    private List<FileHash> getExpiredOnPage(Slice<FileHash> page, Timestamp currentTime) {
        return page.getContent().stream().filter(el -> el.getExpiredAt().before(currentTime)).toList();
    }
}
