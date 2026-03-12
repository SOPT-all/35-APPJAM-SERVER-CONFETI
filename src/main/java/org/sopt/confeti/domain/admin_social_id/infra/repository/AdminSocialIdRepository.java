package org.sopt.confeti.domain.admin_social_id.infra.repository;

import org.sopt.confeti.domain.admin_social_id.AdminSocialId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSocialIdRepository extends JpaRepository<AdminSocialId, Long> {

    boolean existsBySocialId(String socialId);
}
