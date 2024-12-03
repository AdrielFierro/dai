package com.tpo.TPO.service;

import com.tpo.TPO.entity.Favorite;
import com.tpo.TPO.entity.Post;
import com.tpo.TPO.exceptions.FavoriteNotFoundException;
import com.tpo.TPO.repository.FavoriteRepository;
import com.tpo.TPO.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private PostRepository postRepository;

    public List<Post> favoritePostByUserId(Integer userId) {
        List<Favorite> favorites = favoriteRepository.findFavoriteByUserId(userId);

        if (favorites.isEmpty()) {
            // Manejar el caso en que no se encuentren favoritos para el usuario
            return Collections.emptyList();
        }
        List<Integer> postIds = favorites.stream().map(Favorite::getPostId).collect(Collectors.toList());
        List<Post> posts = postRepository.findAllById(postIds);
        if (posts.isEmpty()) {
            // Manejar el caso en que no se encuentren publicaciones correspondientes
            return Collections.emptyList();
        }

        return posts;
    }

    public Favorite addFavorite(Favorite favorite) {

        return favoriteRepository.addFavorite(favorite);
    }
    public int deleteFavoriteByUserIdAndPostId(Integer userId, Integer postId) {
        Optional<Integer> favoriteId = favoriteRepository.findIdByUserIdAndPostId(userId, postId);

        if (favoriteId.isPresent()) {
            favoriteRepository.deleteById(favoriteId.get());
            return favoriteId.get();
        } else {
            return -1; // Retorna -1 si no se encuentra
        }
    }

}