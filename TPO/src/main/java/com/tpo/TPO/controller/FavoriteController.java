package com.tpo.TPO.controller;

import com.tpo.TPO.controller.dto.FavoriteDTO;
import com.tpo.TPO.entity.Favorite;
import com.tpo.TPO.entity.Post;
import com.tpo.TPO.service.FavoriteService;
import com.tpo.TPO.service.PostService;
import com.tpo.TPO.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getFavoritesByUserId(@PathVariable Integer userId) {
        try {
            if (userId == null || userId <= 0) {
                return ResponseEntity.badRequest().build();
            }

            List<Post> favoritePosts = favoriteService.favoritePostByUserId(userId);

            if (favoritePosts.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(favoritePosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteDTO favoriteDTO) {
        try {
            if (favoriteDTO.getUserId() == null || favoriteDTO.getPostId() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Verificar que el PostID existe
            Optional<Post> post = postService.getPostById(favoriteDTO.getPostId());
            if (post.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El PostID no existe");
            }

            // Verificar que el UserID existe
            if (userService.getUserById(favoriteDTO.getUserId()) == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("El UserID no existe");
            }

            // Crear entidad Favorite a partir del DTO
            Favorite favorite = new Favorite();
            favorite.setUserId(favoriteDTO.getUserId());
            favorite.setPostId(favoriteDTO.getPostId());

            Favorite addedFavorite = favoriteService.addFavorite(favorite);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedFavorite);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}