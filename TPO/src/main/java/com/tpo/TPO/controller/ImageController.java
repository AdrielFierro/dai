package com.tpo.TPO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.azure.core.util.BinaryData;
import com.tpo.TPO.controller.dto.ImageDTO;
import com.tpo.TPO.entity.Post;
import com.tpo.TPO.service.ImageService;
import com.tpo.TPO.service.PostService;

import jakarta.mail.Multipart;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

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
