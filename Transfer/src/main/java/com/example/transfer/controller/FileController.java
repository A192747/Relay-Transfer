package com.example.transfer.controller;

import com.example.transfer.service.HashService;
import com.example.transfer.service.LinkService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {
    private final LinkService linkService;

    @PostMapping("/files")
    public ResponseEntity<String> saveFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(linkService.save(file));
    }

    @GetMapping("/{hash}")
    public ResponseEntity<StreamingResponseBody> getFile(@PathVariable("hash") String hash) {
        File tmp = linkService.getFileByHash(hash);
        StreamingResponseBody stream = outputStream -> {
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(tmp))) {
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + tmp.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(stream);
    }
}
