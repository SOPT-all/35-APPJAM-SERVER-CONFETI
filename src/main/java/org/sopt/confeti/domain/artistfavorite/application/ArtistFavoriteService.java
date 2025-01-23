package org.sopt.confeti.domain.artistfavorite.application;

import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.artistfavorite.infra.repository.ArtistFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.util.artistsearcher.ArtistResolver;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ArtistFavoriteService {
    ArtistFavoriteRepository artistFavoriteRepository;
    private final ArtistResolver artistResolver;

    @Transactional(readOnly = true)
    public List<ArtistFavorite> getArtistList(Long userId) {
        List<ArtistFavorite> artistList = artistFavoriteRepository.findTop6ByUserIdOrderByRand(userId);
        artistResolver.load(artistList);

        return artistList;
    }

    @Transactional(readOnly = true)
    public boolean isFavorite(final long userId, final String artistId) {
        return artistFavoriteRepository.existsByUserIdAndArtist_ArtistId(userId, artistId);
    }

    @Transactional
    public void addFavorite(final User user, final String artistId) {
        artistFavoriteRepository.save(
                ArtistFavorite.create(user, artistId)
        );
    }

    @Transactional
    public void removeFavorite(final long userId, final String artistId) {
        artistFavoriteRepository.deleteByUserIdAndArtist_ArtistId(userId, artistId);
    }

    @Transactional(readOnly = true)
    public boolean existsByUserId(final long userId) {
        return artistFavoriteRepository.existsByUserId(userId);
    }
}
