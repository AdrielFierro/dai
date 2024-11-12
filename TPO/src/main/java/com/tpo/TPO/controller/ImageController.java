package com.tpo.TPO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.azure.core.util.BinaryData;
import com.tpo.TPO.service.ImageService;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/image")
public class ImageController {

    @Autowired
    private ImageService imageservice;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file)
            throws IOException {
        try (InputStream inputStream = file.getInputStream()) {

            @SuppressWarnings("null")
            String imageurl = imageservice.uploadImage(file.getOriginalFilename().toString(), inputStream,
                    file.getSize());

            return ResponseEntity.ok(imageurl);
        }

    }

    @GetMapping("/download")
    public ResponseEntity<BinaryData> downloadFile(@RequestBody String url)
            throws IOException {

        return imageservice.downloadImage(url);

    }

}
