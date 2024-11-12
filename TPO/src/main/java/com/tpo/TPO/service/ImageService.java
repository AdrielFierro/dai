package com.tpo.TPO.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tpo.TPO.repository.ImageRepository;
import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

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

    public ArrayList<String> fileToURL(List<MultipartFile> imagesPost) throws IOException {

        ArrayList<String> urls = new ArrayList<String>();

        for (MultipartFile mf : imagesPost) {
            InputStream data = mf.getInputStream();
            String filename = mf.getOriginalFilename();
            long lenght = mf.getSize();
            String url = this.uploadImage(filename, data, lenght);
            urls.add(url);
        }

        return urls;
    }

}
