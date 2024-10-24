package com.tpo.TPO.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Post {

    public Post() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private List<Integer> usersLikes;
    // Puedes agregar más relaciones si es necesario
}
