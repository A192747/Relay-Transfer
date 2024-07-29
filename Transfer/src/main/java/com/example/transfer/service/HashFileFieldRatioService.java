package com.example.transfer.service;

import com.example.transfer.model.FileHash;
import com.example.transfer.repository.HashFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashFileFieldRatioService {
    private final HashFileRepository hashFileRepository;
    public FileHash save(FileHash fileHash) {
        return hashFileRepository.save(fileHash);
    }

    public FileHash getByHash(String hash) {
        System.out.println(hash);
        return hashFileRepository.findFileHashByHash(hash);
    }

    public void deleteByHash(String hash) {
        hashFileRepository.deleteFileHashByHash(hash);
    }
}
