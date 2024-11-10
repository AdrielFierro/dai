package com.tpo.TPO.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.tpo.TPO.entity.Image;
import com.tpo.TPO.repository.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    // public ImageRepository(BlobServiceClient blobServiceClient) {
    // this.blobServiceClient = blobServiceClient;
    // }

    private final String containerName = "imagecontainer";
    private final String constr = "DefaultEndpointsProtocol=https;AccountName=latticestorageaccount1;AccountKey=PDthXHKwTJwgTBXk29UKQeW44GQAauZkvzrKIhY6bSIjlNCjbEA0DyiHql5e5P8FdLR5TjJWypgc+AStlmgDZA==;EndpointSuffix=core.windows.net";

    public String uploadImage(Integer userId, Integer postId, String imageName, long length, InputStream data)
            throws IOException {

        // // Get the BlobContainerClient object to interact with the container
        // BlobContainerClient blobContainerClient =
        // blobServiceClient.getBlobContainerClient(containerName);

        // // Rename the image file to a unique name
        // String newImageName = UUID.randomUUID().toString() +
        // imageName.substring(imageName.lastIndexOf("."));

        // // Get the BlobClient object to interact with the specified blob
        // BlobClient blobClient = blobContainerClient.getBlobClient(newImageName);

        // // Upload the image file to the blob
        // blobClient.upload(data, length, true);

        // return blobClient.getBlobUrl();

        return "wip";

    }
}
