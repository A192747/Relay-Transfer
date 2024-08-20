package com.example.transfer.repository;

import com.example.transfer.model.FileHash;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HashFileRepository extends CassandraRepository<FileHash, UUID> {
    @Query("SELECT * FROM file_hash WHERE hash=?0 ALLOW FILTERING")
    FileHash findFileHashByHash(String hash);

    void deleteFileHashById(UUID id);

}
