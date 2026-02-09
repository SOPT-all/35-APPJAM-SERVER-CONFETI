package org.sopt.confeti.domain.music.relatedartist.application;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.artist.Artist;
import org.sopt.confeti.domain.music.artist.infra.repository.ArtistRepository;
import org.sopt.confeti.domain.music.relatedartist.RelatedArtist;
import org.sopt.confeti.domain.music.relatedartist.infra.repository.RelatedArtistRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.domain.music.relatedartist.application.dto.RelatedArtistInfo;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelatedArtistService {

    private final RelatedArtistRepository relatedArtistRepository;
    private final ArtistRepository artistRepository;

    @ReadOnlyTransactional
    public List<ConfetiArtist> getRelatedArtists(String artistId, int limit) {
        return relatedArtistRepository.findAllByArtistId(artistId, PageRequest.of(0, limit)).stream()
            .map(RelatedArtist::getRelatedArtist)
            .map(Artist::toConfetiArtist)
            .toList();
    }

    @ReadOnlyTransactional
    public List<RelatedArtistInfo> getRelatedArtistInfos(String artistId, int limit) {
        return relatedArtistRepository.findAllByArtistId(artistId, PageRequest.of(0, limit)).stream()
            .map(RelatedArtist::toDomain)
            .toList();
    }

    @Transactional
    public void createRelatedArtists(String artistId, Set<String> relatedArtistIds) {
        Set<String> existingRelatedArtistIds = relatedArtistRepository.findRelatedArtistIdsByArtistId(artistId);
        Artist artist = artistRepository.getReferenceById(artistId);

        List<RelatedArtist> newRelatedArtists = relatedArtistIds.stream()
            .filter(id -> !existingRelatedArtistIds.contains(id))
            .map(id -> RelatedArtist.create(artist, artistRepository.getReferenceById(id)))
            .toList();

        relatedArtistRepository.saveAll(newRelatedArtists);
    }
}
