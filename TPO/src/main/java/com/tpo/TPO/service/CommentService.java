package com.tpo.TPO.service;

import com.tpo.TPO.controller.dto.CommentDTO;
import com.tpo.TPO.entity.Comment;
import com.tpo.TPO.exceptions.NoCommentFound;
import com.tpo.TPO.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // Retrieve all comments of a specific post
    public List<Comment> getCommentsByPostId(Integer postId) {

        return commentRepository.findByPostId(postId);
    }

    // Post a new comment to a post
    public Comment createComment(Integer postId, CommentDTO comment, Integer userId) {
        String comentario = comment.getComment();

        // Asigna la fecha y hora actual a timestamp
        LocalDateTime currentTimestamp = LocalDateTime.now();

        Comment createdComment = Comment.builder()
                .comment(comentario)
                .postId(postId)
                .userId(userId)
                .timestamp(currentTimestamp)  // Mapea el timestamp actual
                .build();

        return commentRepository.save(createdComment);
    }

    // Delete a specific comment of a post
    public boolean deleteComment(Integer postId, Integer commentId) throws NoCommentFound {
        Optional<Comment> comment = commentRepository.findByPostIdAndCommentId(postId, commentId);
        if (comment.isPresent()) {
            commentRepository.deleteById(commentId);
            return true;
        }
        throw new NoCommentFound();
    }

    // Get a specific comment
    public Optional<Comment> getComment(Integer postId, Integer commentId) throws NoCommentFound {

        Optional<Comment> comment = commentRepository.findByPostIdAndCommentId(postId, commentId);

        if (comment.isPresent()) {
            return comment;
        }
        throw new NoCommentFound();

    }

    public void deleteAllCommentsByUser(Integer userId) {
        // Obtener todos los comentarios del usuario
        List<Comment> userComments = commentRepository.findByUserId(userId);

        // Eliminar todos los comentarios del usuario
        if (!userComments.isEmpty()) {
            commentRepository.deleteAll(userComments);
        }
    }

    // Contar comentarios por UserId
    public int countCommentsByUserId(Integer userId) {
        return commentRepository.countByUserId(userId);
    }

    public int countCommentsByPostId(Integer postId) {
        if (postId == null) {
            throw new IllegalArgumentException("El postId no puede ser nulo");
        }

        int count = commentRepository.countByPostId(postId);
        if (count == 0) {
            // Cambiar la respuesta en lugar de lanzar una excepción
            // return -1; o manejar de otra forma la ausencia
        }
        return count;
    }
}
