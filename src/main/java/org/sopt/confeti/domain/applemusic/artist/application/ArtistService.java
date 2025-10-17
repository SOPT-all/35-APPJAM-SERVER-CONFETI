package org.sopt.confeti.domain.applemusic.artist.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.artist.Artist;
import org.sopt.confeti.domain.applemusic.artist.application.dto.request.CreateArtistDTO;
import org.sopt.confeti.domain.applemusic.artist.infra.repository.ArtistRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public Long create(CreateArtistDTO createArtistDTO) {
        Artist artist = createArtistDTO.toArtist();
        return artistRepository.save(artist).getId();
    }

    public boolean isExistByArtistId(String artistId) {
        return artistRepository.existsById(artistId);
    }

}
