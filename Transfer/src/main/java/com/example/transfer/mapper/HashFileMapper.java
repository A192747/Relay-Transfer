package com.example.transfer.mapper;

import com.example.transfer.model.FileHash;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class HashFileMapper {
    public FileHash toEntity(String fileName, String hash, Timestamp expirationDate) {
        FileHash fileHash = new FileHash();
        fileHash.setId(UUID.randomUUID());
        fileHash.setHash(hash);
        fileHash.setExpiredAt(expirationDate);
        fileHash.setFileName(fileName);
        return fileHash;
    }
}
