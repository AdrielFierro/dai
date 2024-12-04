package com.tpo.TPO.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@Data
public class CommentResponseDTO {

    private Integer commentId;
    private Integer postId;
    private Integer userId;
    private String comment;
    private LocalDateTime timestamp;
    private String timeAgo;

    public CommentResponseDTO(Integer commentId, Integer postId, Integer userId, String comment, LocalDateTime timestamp, String timeAgo) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.comment = comment;
        this.timestamp = timestamp;
        this.timeAgo = timeAgo;
    }

    // Getters y Setters
}