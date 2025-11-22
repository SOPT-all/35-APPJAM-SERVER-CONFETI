package org.sopt.confeti.domain.applemusic.relatedartist.topartist.infra.repository;

import org.sopt.confeti.domain.applemusic.relatedartist.topartist.RelatedArtist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelatedArtistRepository extends JpaRepository<RelatedArtist, Long> {

}
