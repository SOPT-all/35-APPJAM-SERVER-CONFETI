package org.sopt.confeti.domain.admin_social_id.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.admin_social_id.infra.repository.AdminSocialIdRepository;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSocialIdService {

    private final AdminSocialIdRepository adminSocialIdRepository;

    @ReadOnlyTransactional
    public boolean isAdminSocialId(String socialId, OAuthProvider provider) {
        return adminSocialIdRepository.existsBySocialIdAndProvider(socialId, provider);
    }
}
