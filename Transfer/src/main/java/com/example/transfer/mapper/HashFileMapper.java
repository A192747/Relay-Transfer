package com.example.transfer.mapper;

import com.example.transfer.model.FileHash;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class HashFileMapper {
    public FileHash toEntity(String fileName, String hash) {
        FileHash fileHash = new FileHash();
        fileHash.setId(UUID.randomUUID());
        fileHash.setHash(hash);
        fileHash.setFileName(fileName);
        return fileHash;
    }
}
