package org.sopt.confeti.domain.music.topartist.application;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.topartist.TopArtist;
import org.sopt.confeti.domain.music.topartist.infra.repository.TopArtistRepository;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopArtistService {

    private final TopArtistRepository topArtistRepository;

    @Transactional(readOnly = true)
    public List<ConfetiArtist> getTopArtists() {
        return topArtistRepository.findAllWithArtist().stream()
            .map(TopArtist::getArtist)
            .map(Artist::toDomain)
            .toList();
    }

    @Transactional
    public void create(List<ConfetiArtist> confetiArtists) {
        List<Artist> artists = confetiArtists.stream()
            .map(Artist::fromDomain)
            .toList();

        List<TopArtist> topArtists = IntStream.range(0, artists.size()).mapToObj(idx ->
            TopArtist.create(artists.get(idx), idx + 1)
        ).toList();

        topArtistRepository.saveAll(topArtists);
    }
}
