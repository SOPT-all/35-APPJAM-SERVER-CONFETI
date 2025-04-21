package org.sopt.confeti.domain.user.application;

import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.dto.request.PatchUserInfoRequest;
import org.sopt.confeti.auth.dto.CreateUserDTO;
import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.infra.repository.AllUserRepository;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.FileDownloader;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final boolean TIMETABLE_ADDED = true;

    private final UserRepository userRepository;
    private final AllUserRepository allUserRepository;
    private final S3FileHandler s3FileHandler;
    private final FileDownloader fileDownloader;

    @Transactional(readOnly = true)
    public User findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new UnauthorizedException(ErrorMessage.UNAUTHORIZED)
                );
        return user;
    }

    @Transactional(readOnly = true)
    public boolean existsById(Long userId) {
        return userRepository.existsById(userId);
    }

    @Transactional(readOnly = true)
    public User findUserTimetablesById(final long userId) {
        return userRepository.findUserTimetablesById(userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
    }

    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public boolean notExist(String socialId, OAuthProvider provider) {
        return !userRepository.existsBySocialIdAndProvider(socialId, provider);
    }

    @Transactional
    public void create(CreateUserDTO createUserDTO) {
        allUserRepository.save(
                AuthUser.create(
                        createUserDTO.provider(),
                        createUserDTO.id(),
                        createUserDTO.name(),
                        createUserDTO.profileImgUrl()
                )
        );
    }

    @Transactional(readOnly = true)
    public AuthUser getAuthUser(String socialId, OAuthProvider provider) {
        return userRepository.findBySocialIdAndProvider(socialId, provider)
                .map(AuthUser::toAuthUser)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
    }

    @Transactional
    public void patchUserInfo(long userId, PatchUserInfoRequest patchUserInfoRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        String fileName = uploadProfile(patchUserInfoRequest.profileUrl());

        user.setProfilePath(fileName);
        user.setName(patchUserInfoRequest.name());
    }

    public String uploadProfile(String profileImgUrl) {
        Path profileImg = fileDownloader.downloadFile(profileImgUrl);
        try {
            return s3FileHandler.uploadFile(profileImg.toFile(),
                    FolderPath.combine(FolderPath.USER, FolderPath.PROFILE));
        } finally {
            fileDownloader.deleteTempFile(profileImg);
        }
    }

    @Transactional
    public void updateHasTimetableHistory(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        user.setHasTimetableHistory(TIMETABLE_ADDED);
    }
}
