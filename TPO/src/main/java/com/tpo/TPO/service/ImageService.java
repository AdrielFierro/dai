package com.tpo.TPO.service;

import java.util.UUID;

import javax.imageio.ImageIO;

import java.net.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tpo.TPO.repository.ImageRepository;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.awt.image.*;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    private BlobServiceClient blobServiceClient;

    private final String containerName = "imagecontainer";

    public String uploadImage(String originalImageName, InputStream data, long length)
            throws IOException {
        try {

            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(
                    "DefaultEndpointsProtocol=https;AccountName=latticestorageaccount1;AccountKey=PDthXHKwTJwgTBXk29UKQeW44GQAauZkvzrKIhY6bSIjlNCjbEA0DyiHql5e5P8FdLR5TjJWypgc+AStlmgDZA==;EndpointSuffix=core.windows.net")
                    .buildClient();

            BlobContainerClient blobcontainerclient = blobServiceClient.getBlobContainerClient(containerName);

            // String newImageName = UUID.randomUUID().toString()
            // + originalImageName.substring(originalImageName.lastIndexOf(""));

            String newImageName = UUID.randomUUID().toString()
                    + originalImageName;

            BlobClient blobClient = blobcontainerclient.getBlobClient(newImageName);
            blobClient.upload(data, length, true);
            return blobClient.getBlobUrl();

        } catch (Exception e) {
            return e.getMessage();

        }

    }

    public ResponseEntity<BinaryData> downloadImage(String url) throws IOException {

        return null;

    }

}
