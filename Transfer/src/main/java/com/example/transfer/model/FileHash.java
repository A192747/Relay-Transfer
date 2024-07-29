package com.example.transfer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Table("file_hash")
public class FileHash {
    @PrimaryKey
    @NotNull
    @Column("id")
    private UUID id;
    @Column("hash")
    @NotBlank
    private String hash;
    @Column("file_name")
    @NotBlank
    private String fileName;
}
