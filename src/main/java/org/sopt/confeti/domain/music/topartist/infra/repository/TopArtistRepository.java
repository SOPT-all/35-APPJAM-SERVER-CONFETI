package org.sopt.confeti.domain.music.topartist.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.music.topartist.TopArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopArtistRepository extends JpaRepository<TopArtist, Long> {

    @Query("SELECT ta FROM TopArtist ta JOIN FETCH ta.artist")
    List<TopArtist> findAllWithArtist();
}
