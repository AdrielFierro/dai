package com.tpo.TPO.controller.dto;

import java.util.Collections;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tpo.TPO.entity.User;

public class UserDTO {
    private String name;
    private String lastName;
    private String userName;
    private String descripcion;

    private MultipartFile urlImage; // No es una lista en el atributo
    private MultipartFile backImage; // No es una lista en el atributo

    // Getter para 'user'


    // Getter para 'urlImage' que transforma a una lista
    public List<MultipartFile> getUrlImage() {
        return urlImage != null ? Collections.singletonList(urlImage) : Collections.emptyList();
    }

    // Getter para 'backImage' que transforma a una lista
    public List<MultipartFile> getBackImage() {
        return backImage != null ? Collections.singletonList(backImage) : Collections.emptyList();
    }

    // Setters para todas las propiedades
    public void setName(String name) {
        this.name = name;
    }

    public void setUrlImage(MultipartFile urlImage) {
        this.urlImage = urlImage;
    }

    public void setBackImage(MultipartFile backImage) {
        this.backImage = backImage;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUsername() {
        return userName;
    }
}
