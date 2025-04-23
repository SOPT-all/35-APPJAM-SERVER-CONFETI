package org.sopt.confeti.domain.setlist.infra.repository;

import java.util.List;
import org.sopt.confeti.domain.setlist.Setlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SetlistRepository extends JpaRepository<Setlist, Long> {

    @Query("SELECT s FROM Setlist s WHERE s.user.id = :userId")
    List<Setlist> findAllByUserId(@Param("userId") Long userId);
}
