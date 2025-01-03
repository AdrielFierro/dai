package com.tpo.TPO.controller;

import com.tpo.TPO.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import com.tpo.TPO.controller.dto.PostDTO;
import com.tpo.TPO.entity.Post;
import com.tpo.TPO.service.ImageService;
import com.tpo.TPO.service.PostService;
import com.tpo.TPO.service.UserService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


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
    public List<String> getImagesFromPost(@PathVariable Integer postId) {

        Optional<Post> post = postService.getPostById(postId);
        Post realPost = post.get();

        return realPost.getImage();

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
        User user = userService.getUserById(userId);
        Set<Post> timeline = new HashSet<>();

        // Obtener posts del propio usuario
        List<Post> userPosts = postService.getPostsByUser(userId);
        timeline.addAll(userPosts);

        // Obtener posts de los usuarios seguidos
        Set<User> followedUsers = userService.getFollowedUsers(user.getFollowedIds());
        for (User followedUser : followedUsers) {
            List<Post> followedUserPosts = postService.getPostsByUser(followedUser.getId());
            timeline.addAll(followedUserPosts);
        }

        // Filtrar los posts con imagen no nula y no vacía
        Set<Post> filteredTimeline = timeline.stream()
                .filter(post -> post.getImage() != null && !post.getImage().isEmpty())
                .collect(Collectors.toSet());

        // Verificar si la lista de timeline filtrada está vacía
        if (filteredTimeline.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Transformar el Set a una List
        List<Post> sortedTimeline = new ArrayList<>(filteredTimeline);

        // Ordenar los posts por fecha (asumiendo que el Post tiene una propiedad `date`)
        sortedTimeline.sort(Comparator.comparing(Post::getFecha).reversed());

        return ResponseEntity.ok(sortedTimeline);
    }

    // Método para obtener posts de un usuario específico
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getPostsByUser(@PathVariable Integer userId) {
        List<Post> posts = postService.getPostsByUser(userId);
        if (posts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<Post> filteredPosts = posts.stream()
                .filter(post -> post.getImage() != null && !post.getImage().isEmpty())
                .collect(Collectors.toList());
        return ResponseEntity.ok(filteredPosts);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Post> addLike(@PathVariable Integer postId, @RequestParam Integer userId) {
        Post updatedPost = postService.addUserLike(postId, userId);
        return ResponseEntity.ok(updatedPost); // Devolver el post actualizado
    }
    
    // Endpoint para eliminar un like
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Post> removeLike(@PathVariable Integer postId, @RequestParam Integer userId) {
        Post updatedPost = postService.removeUserLike(postId, userId);
        return ResponseEntity.ok(updatedPost); // Devuelve el post actualizado
    }



}