package com.tpo.TPO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {

        String filename = file.getOriginalFilename();
        InputStream inputStream = file.getInputStream();

        String uploadImage = imageservice.uploadImage(1, 1, filename, file.getSize(), inputStream);

        if (uploadImage == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(uploadImage);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(uploadImage);
    }

}
