package org.sopt.confeti.domain.artistfavorite.application;

import lombok.AllArgsConstructor;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.artistfavorite.infra.repository.ArtistFavoriteRepository;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.util.artistsearcher.ArtistResolver;
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
        List<ArtistFavorite> artistList = artistFavoriteRepository.findTop3ByUserIdOrderByRand(userId);
        artistResolver.load(artistList);

        return artistList;
    }
}
