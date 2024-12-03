package com.tpo.TPO.repository;

import com.tpo.TPO.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    List<Favorite> findFavoriteByUserId(Integer userId);

    default Favorite addFavorite(Favorite favorite) {
        return save(favorite);
    }

    // Método para encontrar el id del favorito por userId y postId
    @Query("SELECT f.id FROM Favorite f WHERE f.userId = :userId AND f.postId = :postId")
    Optional<Integer> findIdByUserIdAndPostId(@Param("userId") Integer userId, @Param("postId") Integer postId);

    // Método para eliminar un Favorite por su ID
    @Transactional
    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.id = :id")
    void deleteById(@Param("id") Integer id);
}