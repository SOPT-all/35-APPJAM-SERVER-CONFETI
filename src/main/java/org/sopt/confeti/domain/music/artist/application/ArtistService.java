package org.sopt.confeti.domain.music.artist.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.application.dto.request.CreateArtistCommand;
import org.sopt.confeti.domain.music.artist.infra.repository.ArtistRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    @ReadOnlyTransactional
    public List<Artist> getArtists(Set<String> artistIds) {
        return artistRepository.findAllById(artistIds);
    }

    @Transactional
    public String create(CreateArtistCommand createArtistCommand) {
        Artist artist = createArtistCommand.toArtist();
        return artistRepository.save(artist).getId();
    }

    @Transactional
    public void create(List<ConfetiArtist> artists) {
        Set<String> requestedIds = artists.stream()
            .map(ConfetiArtist::getId)
            .collect(Collectors.toSet());

        Set<String> existingIds = artistRepository.findAllById(requestedIds).stream()
            .map(Artist::getId)
            .collect(Collectors.toSet());

        List<Artist> newArtists = artists.stream()
            .filter(artist -> !existingIds.contains(artist.getId()))
            .map(Artist::fromDomain)
            .toList();

        artistRepository.saveAll(newArtists);
    }

    public Artist getReferenceById(String artistId) {
        return artistRepository.getReferenceById(artistId);
    }

    @Transactional(readOnly = true)
    public boolean isExistByArtistId(String artistId) {
        return artistRepository.existsById(artistId);
    }
}
