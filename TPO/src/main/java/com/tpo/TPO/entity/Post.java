package com.tpo.TPO.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    private List<String> image;

    @Column(nullable = false)
    private Integer userId;

    @ElementCollection
    private List<Integer> usersLikes;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = true)
    private String direc;

}
