package com.tpo.TPO.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.tpo.TPO.controller.dto.PostDTO;
import com.tpo.TPO.entity.Post;
import com.tpo.TPO.service.ImageService;
import com.tpo.TPO.service.PostService;
import com.tpo.TPO.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer postId) {
        Optional<Post> post = postService.getPostById(postId);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String authorizationHeader,
            @ModelAttribute PostDTO postDTO)
            throws IOException {

        Integer userId = userService.getIdfromToken(authorizationHeader);

        ArrayList<String> urls = imageService.fileToURL(postDTO.getImagesPost());

        LocalDateTime fecha = LocalDateTime.now();

        Post post = Post.builder().description(postDTO.getDescripcion()).direc(postDTO.getDirec()).image(urls)
                .fecha(fecha)
                .userId(userId).build();

        Post createdPost = postService.createPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @GetMapping("/getImages/{postId}")
    public ArrayList<String> getImagesFromPost(@PathVariable Integer postId) {

        Optional<Post> post = postService.getPostById(postId);
        Post realPost = post.get();
        ArrayList<String> images = realPost.getImage();

        return images;

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Método para obtener el timeline de un usuario
    @GetMapping("/timeline/{userId}")
    public ResponseEntity<List<Post>> getTimeline(@PathVariable Integer userId) {
        List<Post> timelinePosts = postService.getTimeline(userId);
        if (timelinePosts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(timelinePosts);
    }

    // Método para obtener posts de un usuario específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Integer userId) {
        List<Post> posts = postService.getPostsByUser(userId);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(posts);
    }

}
