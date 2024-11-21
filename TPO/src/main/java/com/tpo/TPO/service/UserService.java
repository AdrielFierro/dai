package com.tpo.TPO.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tpo.TPO.controller.config.JwtService;
import com.tpo.TPO.entity.User;
import com.tpo.TPO.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwts;

    public List<User> getAllUsers(String contains, int skip, int limit, String orderby) {
        Pageable pageable = PageRequest.of(skip / limit, limit, Sort.by(orderby));
        return userRepository.findUsers(contains, pageable);
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User createUser(User user) {
        //Para mostrar tambien en el timeline las publicaciones propias
        //user.addFollower(user.getId());
        return userRepository.save(user);
    }

    public User updateUser(Integer userId, User user) {
        // Lógica para actualizar un usuario
        return userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }

    public int getUserCommentsCount(Integer userId) {
        // Lógica para contar los comentarios de un usuario
        return userRepository.countCommentsByUserId(userId);
    }


    // Método para obtener los seguidores de un usuario
    public Set<User> getFollowers(Integer userId) {
        // Obtener el usuario y sus followersIds
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Set<Integer> followersIds = user.getFollowersIds();

        // Buscar todos los usuarios que correspondan a esos followersIds
        return userRepository.findAllByIdIn(followersIds);
    }
 
    public Set<User> getFollowedUsers(Set<Integer> followedIds) {
        return userRepository.findAllByIdIn(followedIds);
    }

    @Transactional
    public User followUser(Integer userId, Integer followUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    
        User userToFollow = userRepository.findById(followUserId)
                .orElseThrow(() -> new RuntimeException("User to follow not found with id: " + followUserId));
        // Asegúrate de que las listas no sean nulas
        if (user.getFollowedIds() == null) {
            user.setFollowedIds(new HashSet<>()); 
            user.getFollowedIds().add(userId); // Inicializa la lista si es nula
        }

        if (userToFollow.getFollowersIds() == null) {
            userToFollow.setFollowersIds(new HashSet<>());  // Inicializa la lista si es nula
        }
        // Verifica si el usuario ya está siguiendo al otro
        if (user.getFollowedIds().contains(followUserId)) {
            throw new IllegalArgumentException("User is already following the target user.");
        }
    
        // Agrega el ID del usuario seguido a la lista de IDs de seguidos del usuario
        user.getFollowedIds().add(followUserId);
        
        // Agrega el ID del usuario a la lista de seguidores del otro usuario
        userToFollow.getFollowersIds().add(userId);
    
        // Guarda ambos usuarios
        userRepository.save(user);
        userRepository.save(userToFollow);
    
        return userToFollow; // Devuelve el usuario seguido
    }
   /*  @Transactional
    public User followUser(Integer userId, Integer followUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User userToFollow = userRepository.findById(followUserId)
                .orElseThrow(() -> new RuntimeException("User to follow not found"));

        if (user.getFollowed().contains(userToFollow)) {
            throw new IllegalArgumentException("User is already followed");
        }

        user.getFollowed().add(userToFollow);
        userToFollow.getFollowers().add(user);

        userRepository.save(user);
        userRepository.save(userToFollow);

        return userToFollow; // Devuelve el usuario seguido
    }
*/
    public boolean isEmailUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUsernameUsed(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public Integer getIdfromToken(String accessToken) {

        accessToken = accessToken.substring(7); // Eliminar "Bearer "
        int idusuario = jwts.extractId(accessToken);

        return idusuario;

    }
    
    public List<User> getRandomUsers(Integer userId) {
        return userRepository.findRandomUsers(userId); // Pasamos el userId
    }
    
    
    public void unfollowUser(Integer userId, Integer unfollowUserId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User userToUnfollow = userRepository.findById(unfollowUserId).orElseThrow(() -> new RuntimeException("User to unfollow not found"));
        
        // Verifica si el usuario ya sigue a este usuario
        if (!user.getFollowedIds().contains(unfollowUserId)) {
            throw new IllegalArgumentException("User is not following the specified user");
        }
        
        // Elimina el ID del usuario seguido de la lista de seguidos
        user.getFollowedIds().remove(unfollowUserId);
        
        // Guarda los cambios
        userRepository.save(user);
    }

    public List<User> searchUsersByUsername(String username) {
        return userRepository.findByUsernameStartingWithIgnoreCase(username);
    }
    
    @Transactional
    public void deleteFollowingByUser(Integer userId) {
        // Buscar el usuario original
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        // Obtener todos los IDs de las personas que sigue el usuario
        Set<Integer> followedIds = user.getFollowedIds();

        // Verificar si el usuario no sigue a nadie
        if (followedIds == null || followedIds.isEmpty()) {
            return; // No hay nada que eliminar
        }

        // Para cada ID seguido, eliminar al usuario de la lista de followers de ese usuario
        for (Integer followedId : followedIds) {
            User followedUser = userRepository.findById(followedId)
                    .orElseThrow(() -> new RuntimeException("Followed user not found with id: " + followedId));

            // Remover el userId de la lista de followers del usuario seguido
            if (followedUser.getFollowersIds() != null) {
                followedUser.getFollowersIds().remove(userId);
            }

            // Guardar el usuario seguido actualizado
            userRepository.save(followedUser);
        }

        // Limpiar la lista de seguidos del usuario original
        //user.setFollowedIds(new HashSet<>());
        //userRepository.save(user); // Guardar el usuario original actualizado
    }

    @Transactional
public void deleteFollowersByUser(Integer userId) {
    // Buscar el usuario original
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

    // Obtener todos los IDs de los seguidores
    Set<Integer> followersIds = user.getFollowersIds();

    // Verificar si el usuario no tiene seguidores
    if (followersIds == null || followersIds.isEmpty()) {
        return; // No hay nada que eliminar
    }

    // Para cada ID de seguidor, eliminar al usuario original de su lista de seguidos
    for (Integer followerId : followersIds) {
        User followerUser = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("Follower user not found with id: " + followerId));

        // Remover el userId de la lista de seguidos del usuario seguidor
        if (followerUser.getFollowedIds() != null) {
            followerUser.getFollowedIds().remove(userId);
        }

        // Guardar el usuario seguidor actualizado
        userRepository.save(followerUser);
    }

    // Limpiar la lista de seguidores del usuario original
    //user.setFollowersIds(new HashSet<>());
    //userRepository.save(user); // Guardar el usuario original actualizado
}

}
