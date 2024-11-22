package com.tpo.TPO.repository;

import com.tpo.TPO.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    List<Favorite> findFavoriteByUserId(Integer userId);

    default Favorite addFavorite(Favorite favorite) {
        return save(favorite);
    }
}