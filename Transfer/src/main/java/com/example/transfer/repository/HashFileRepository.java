package com.example.transfer.repository;

import com.example.transfer.model.FileHash;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HashFileRepository extends CassandraRepository<FileHash, UUID> {
}
