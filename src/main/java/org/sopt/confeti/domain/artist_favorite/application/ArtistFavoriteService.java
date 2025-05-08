package org.sopt.confeti.domain.artist_favorite.application;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    public List<ArtistFavorite> getFavoriteArtistsPreview(Long userId) {
        List<ArtistFavorite> artistList = artistFavoriteRepository.findTop4ByUserIdOrderByRand(userId);
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
    public void addFavorites(final User user, final Set<String> artistIds) {
        Set<ArtistFavorite> artistFavorites = artistIds.stream()
                .map(artistId -> ArtistFavorite.create(user, artistId))
                .collect(Collectors.toSet());

        artistFavoriteRepository.saveAll(artistFavorites);
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

    @Transactional(readOnly = true)
    public List<ArtistFavorite> getFavoriteArtists(Long userId, String sortBy) {
        List<ArtistFavorite> artistList = artistFavoriteRepository.findArtistFavoritesByUserId(userId);
        musicAPIResolver.load(artistList);

        if ("createdAt".equalsIgnoreCase(sortBy)) {
            artistList.sort(Comparator.comparing(ArtistFavorite::getCreatedAt).reversed());
        } else if ("alphabetically".equalsIgnoreCase(sortBy)) {
            artistList.sort(Comparator.comparing(artist -> artist.getArtist().getName()));
        }

        return artistList;
    }
}
