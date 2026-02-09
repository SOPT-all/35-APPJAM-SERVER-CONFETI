package org.sopt.confeti.domain.music.artist.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.application.dto.ArtistInfo;
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

    @Transactional
    public void createFromArtistInfos(List<ArtistInfo> artistInfos) {
        Set<String> requestedIds = artistInfos.stream()
            .map(ArtistInfo::id)
            .collect(Collectors.toSet());

        Set<String> existingIds = artistRepository.findAllById(requestedIds).stream()
            .map(Artist::getId)
            .collect(Collectors.toSet());

        List<Artist> newArtists = artistInfos.stream()
            .filter(info -> !existingIds.contains(info.id()))
            .map(info -> Artist.create(info.id(), info.name(), info.artworkUrl()))
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
