package com.example.transfer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.UUID;

@Table("file_hash")
@Data
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
    @Column("expired_at")
    @NotNull
    @NotEmpty(message = "Дата должна быть указана")
    private Timestamp expiredAt;
}
