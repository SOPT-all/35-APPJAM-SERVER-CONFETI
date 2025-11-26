package org.sopt.confeti.domain.music.relatedartist.infra.repository;

import org.sopt.confeti.domain.music.relatedartist.RelatedArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelatedArtistRepository extends JpaRepository<RelatedArtist, Long> {

}
