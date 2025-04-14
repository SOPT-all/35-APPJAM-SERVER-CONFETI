package org.sopt.confeti.domain.artist_favorite.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistFavoriteRepository extends JpaRepository<ArtistFavorite, Long> {
    @Query(value = "select * from artist_favorites where user_id = :userId order by rand() limit 4", nativeQuery = true)
    List<ArtistFavorite> findTop4ByUserIdOrderByRand(@Param("userId") Long userId);

    boolean existsByUserIdAndArtist_id(long userId, String artistId);

    void deleteByUserIdAndArtist_id(final long userId, final String artistId);

    boolean existsByUserId(final long userId);

    @Query(
            value = "SELECT af FROM ArtistFavorite af JOIN FETCH af.user u WHERE u.id = :userId"
    )
    List<ArtistFavorite> findArtistFavoritesByUserId(final @Param("userId") long userId);
}
