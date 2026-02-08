package org.sopt.confeti.domain.music.relatedartist.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.relatedartist.RelatedArtist;
import org.sopt.confeti.domain.music.relatedartist.infra.repository.RelatedArtistRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatedArtistService {

    private final RelatedArtistRepository relatedArtistRepository;

    @ReadOnlyTransactional
    public List<ConfetiArtist> getRelatedArtists(String artistId, int limit) {
        return relatedArtistRepository.findAllByArtistId(artistId, PageRequest.of(0, limit)).stream()
            .map(RelatedArtist::getRelatedArtist)
            .map(Artist::toConfetiArtist)
            .toList();
    }
}
