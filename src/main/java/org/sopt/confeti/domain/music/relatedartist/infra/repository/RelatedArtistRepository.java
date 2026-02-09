package org.sopt.confeti.domain.music.relatedartist.infra.repository;

import java.util.List;
import java.util.Set;
import org.sopt.confeti.domain.music.relatedartist.RelatedArtist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RelatedArtistRepository extends JpaRepository<RelatedArtist, Long> {

    @Query("SELECT ra FROM RelatedArtist ra JOIN FETCH ra.relatedArtist WHERE ra.artist.id = :artistId")
    List<RelatedArtist> findAllByArtistId(@Param("artistId") String artistId, Pageable pageable);

    @Query("SELECT ra.relatedArtist.id FROM RelatedArtist ra WHERE ra.artist.id = :artistId")
    Set<String> findRelatedArtistIdsByArtistId(@Param("artistId") String artistId);
}
