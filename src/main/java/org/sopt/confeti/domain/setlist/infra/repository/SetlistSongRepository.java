package org.sopt.confeti.domain.setlist.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistSong;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetlistSongRepository extends JpaRepository<SetlistSong, Long> {

    List<SetlistSong> findBySetlist(Setlist setlist);
}
