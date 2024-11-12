package com.tpo.TPO.controller.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class PostDTO {

    private String descripcion;
    private List<MultipartFile> imagesPost;
    private String direc;

}
