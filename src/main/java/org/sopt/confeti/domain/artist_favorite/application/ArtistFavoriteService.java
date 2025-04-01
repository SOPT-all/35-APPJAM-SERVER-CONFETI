package org.sopt.confeti.domain.artist_favorite.application;

import java.util.List;
import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.artist_favorite.infra.repository.ArtistFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.resolver.music_api.MusicAPIResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ArtistFavoriteService {
    ArtistFavoriteRepository artistFavoriteRepository;
    private final MusicAPIResolver musicAPIResolver;

    @Transactional(readOnly = true)
    public List<ArtistFavorite> getArtistListPreview(Long userId) {
        List<ArtistFavorite> artistList = artistFavoriteRepository.findTop3ByUserIdOrderByRand(userId);
        musicAPIResolver.load(artistList);

        return artistList;
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(final long userId, final String artistId) {
        return artistFavoriteRepository.existsByUserIdAndArtist_id(userId, artistId);
    }

    @Transactional
    public void addFavorite(final User user, final String artistId) {
        artistFavoriteRepository.save(
                ArtistFavorite.create(user, artistId)
        );
    }

    @Transactional
    public void removeFavorite(final long userId, final String artistId) {
        artistFavoriteRepository.deleteByUserIdAndArtist_id(userId, artistId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserId(final long userId) {
        return artistFavoriteRepository.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<ArtistFavorite> getArtistIdsByUserId(final long userId) {
        return artistFavoriteRepository.findArtistFavoritesByUserId(userId);
    }
}
