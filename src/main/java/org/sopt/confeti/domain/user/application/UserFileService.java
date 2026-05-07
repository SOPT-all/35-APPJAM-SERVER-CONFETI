package org.sopt.confeti.domain.user.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.UserFileInfo;
import org.sopt.confeti.global.common.CdnFileDomainResolveService;
import org.sopt.confeti.global.interceptor.auth.UserInfo;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFileService {

    private final CdnFileDomainResolveService cdnFileDomainResolveService;

    public UserFileInfo getFileInfo(UserInfo userInfo) {
        return UserFileInfo.builder()
            .profileUrl(cdnFileDomainResolveService.resolve(userInfo.profilePath()))
            .build();
    }
}
