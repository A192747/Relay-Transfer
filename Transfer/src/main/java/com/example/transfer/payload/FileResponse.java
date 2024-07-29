package com.example.transfer.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.InputStreamResource;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode()
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class FileResponse implements Serializable {
    String filename;
    String contentType;
    Long fileSize;
    Date createdTime;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    transient InputStreamResource stream;
}
