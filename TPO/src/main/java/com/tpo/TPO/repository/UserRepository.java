package com.tpo.TPO.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tpo.TPO.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

        // Método para encontrar usuarios
        @Query("SELECT u FROM User u WHERE u.name LIKE %:contains%")
        List<User> findUsers(@Param("contains") String contains, Pageable pageable);

        // Método para obtener comentarios x User Id
        @Query("SELECT COUNT(c) FROM Comment c WHERE c.userId = :id_user")
        int countCommentsByUserId(Integer id_user);

        // Método para obtener los seguidores de un usuario
        @Query("SELECT u FROM User u WHERE :userId MEMBER OF u.followersIds")
        Set<User> getFollowers(@Param("userId") Integer userId);


        // Método para obtener los usuarios seguidos por un usuario
        @Query("SELECT u FROM User u WHERE :userId MEMBER OF u.followedIds")
        Set<User> getFollowed(@Param("userId") Integer userId);

        // Consulta para obtener usuarios aleatorios
        @Query(value = "SELECT * FROM User u ORDER BY RAND() LIMIT 5", nativeQuery = true)
        List<User> findRandomUsers();

        Optional<User> findByName(String name);

        Optional<User> findByUsername(String username);

        Optional<User> findByEmail(String email);
        
        List<User> findByUsernameStartingWithIgnoreCase(String username);

        Set<User> findAllByIdIn(Set<Integer> ids);


}
