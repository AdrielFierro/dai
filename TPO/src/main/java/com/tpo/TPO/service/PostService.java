package com.tpo.TPO.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tpo.TPO.entity.Post;
import com.tpo.TPO.repository.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(Integer postId) {
        return postRepository.findById(postId);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    // Método para obtener todos los posts de un usuario específico
    public List<Post> getPostsByUser(Integer userId) {
        return postRepository.findByUserId(userId);
    }

    public List<Post> getPostsByUsers(Set<Integer> userIds) {
        return postRepository.findAllByUserIdIn(userIds);
    }

    public Post addUserLike(Integer postId, Integer userId) {
        // Buscar el post por ID
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        // Verificar si el userId ya está en la lista de likes
        if (!post.getUsersLikes().contains(userId)) {
            // Agregar el userId a la lista de likes
            post.getUsersLikes().add(userId);
            postRepository.save(post); // Guardar el post actualizado
        }

        return post;
    }

     // Método para quitar un like
    public Post removeUserLike(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));

        // Verificar si el userId está en la lista de likes y eliminarlo
        if (post.getUsersLikes().contains(userId)) {
            post.getUsersLikes().remove(userId);
            postRepository.save(post); // Guardar el post actualizado
        }

        return post;
    }

    public void deleteAllPostsByUser(Integer userId) {
        List<Post> posts = postRepository.findByUserId(userId);
        postRepository.deleteAll(posts);
    }

    public void deleteAllLikesByUser(Integer userId) {
        // Obtener todos los posts disponibles
        List<Post> allPosts = getAllPosts();
    
        // Recorrer cada post
        for (Post post : allPosts) {
            // Si el usuario dio like al post, removerlo
            if (post.getUsersLikes() != null && post.getUsersLikes().contains(userId)) {
                removeUserLike(post.getPostId(), userId); // Reutilizamos el servicio existente
            }
        }
    }
    public List<Post> getPostsByUserId(Integer userId) {
        return postRepository.findByUserId(userId);
    }
}
