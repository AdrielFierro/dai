package com.tpo.TPO.controller;

import com.tpo.TPO.entity.Comment;
import com.tpo.TPO.exceptions.NoCommentFound;
import com.tpo.TPO.exceptions.NoMatchUserException;
import com.tpo.TPO.service.CommentService;
import com.tpo.TPO.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Autowired
    UserService userService;

    // Retrieve all comments of a specific post
    @GetMapping
    public ResponseEntity<List<Comment>> getCommentsPostID(@PathVariable Integer postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        if (comments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(comments);
    }

    // Post a new comment to a post
    @PostMapping
    public ResponseEntity<Comment> postCommentsPostID(@PathVariable Integer postId, @RequestBody Comment comment,
            @RequestHeader("Authorization") String authorizationHeader) {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        Comment createdComment = commentService.createComment(postId, comment, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    // Delete a specific comment of a post
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer postId, @PathVariable Integer commentId,
            @RequestHeader("Authorization") String authorizationHeader) throws NoMatchUserException, NoCommentFound {

        try {

            Optional<Comment> commentToDelete = commentService.getComment(postId, commentId);
            Comment commentToDeleteReal = commentToDelete.get();
            Integer userIdcommentToDelete = commentToDeleteReal.getUserId();
            Integer userId = userService.getIdfromToken(authorizationHeader);

            if (userIdcommentToDelete == userId) {

                boolean deleted = commentService.deleteComment(postId, commentId);

                return ResponseEntity.ok().body(deleted);

            }
            throw new NoMatchUserException();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e);
        }

    }
}
