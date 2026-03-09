package org.sopt.confeti.domain.performancedraft.infra;

import java.util.List;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PerformanceDraftRepository extends JpaRepository<PerformanceDraft, Long> {

    @Query(
        value = "SELECT * FROM performance_drafts " +
                "WHERE JSON_UNQUOTE(JSON_EXTRACT(performance_data, '$.title')) LIKE CONCAT('%', :keyword, '%') " +
                "   OR JSON_UNQUOTE(JSON_EXTRACT(performance_data, '$.area'))  LIKE CONCAT('%', :keyword, '%')",
        nativeQuery = true
    )
    List<PerformanceDraft> searchByKeyword(@Param("keyword") String keyword);
}
