package org.sopt.confeti.domain.setlist.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetlistMusicRepository extends JpaRepository<SetlistMusic, Long> {
    List<SetlistMusic> findBySetlist(Setlist setlist);
}
