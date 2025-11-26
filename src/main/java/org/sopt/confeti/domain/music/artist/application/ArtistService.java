package org.sopt.confeti.domain.music.artist.application;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.application.dto.request.CreateArtistDTO;
import org.sopt.confeti.domain.music.artist.infra.repository.ArtistRepository;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Transactional(readOnly = true)
    public List<Artist> getArtists(Set<String> artistIds) {
        return artistRepository.findAllById(artistIds);
    }

    @Transactional
    public String create(CreateArtistDTO createArtistDTO) {
        Artist artist = createArtistDTO.toArtist();
        return artistRepository.save(artist).getId();
    }

    @Transactional
    public void create(List<ConfetiArtist> confetiArtists) {
        List<Artist> artists = confetiArtists.stream()
            .map(ConfetiArtist::toArtist)
            .toList();

        artistRepository.saveAll(artists);
    }

    @Transactional(readOnly = true)
    public boolean isExistByArtistId(String artistId) {
        return artistRepository.existsById(artistId);
    }
}
