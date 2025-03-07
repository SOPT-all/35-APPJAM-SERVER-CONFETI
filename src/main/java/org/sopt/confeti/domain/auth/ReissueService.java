package org.sopt.confeti.domain.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.auth.jwt.JwtTokenExtractor;
import org.sopt.confeti.domain.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.auth.jwt.JwtTokenValidator;
import org.sopt.confeti.domain.auth.jwt.TokenParser;
import org.sopt.confeti.domain.token.RefreshToken;
import org.sopt.confeti.domain.token.infra.RefreshTokenRepository;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReissueService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenParser tokenParser;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtTokenExtractor jwtTokenExtractor;

    @Transactional
    public Token reissue(String refreshToken) {
        RefreshToken validatedRefreshToken = getValidatedRefreshToken(refreshToken);
        String userId = jwtTokenExtractor.getSubject(validatedRefreshToken.getRefreshToken());
        Role role = jwtTokenExtractor.getRole(validatedRefreshToken.getRefreshToken());
        OAuthProvider provider = jwtTokenExtractor.getProvider(validatedRefreshToken.getRefreshToken());

        String newAccessToken = jwtTokenGenerator.createAccessToken(userId, role, provider);
        String newRefreshToken = jwtTokenGenerator.createRefreshToken(userId, role, provider);

        refreshTokenRepository.save(RefreshToken.of(newRefreshToken, Long.parseLong(userId)));
        refreshTokenRepository.deleteAllByRefreshTokenAndUserId(validatedRefreshToken.getRefreshToken(), Long.parseLong(userId));

        return new Token(newAccessToken, newRefreshToken);
    }

    private RefreshToken getValidatedRefreshToken(String refreshToken) {
        String parsedToken = tokenParser.getToken(refreshToken);
        jwtTokenValidator.validate(parsedToken);
        RefreshToken token = refreshTokenRepository.findByRefreshToken(parsedToken)
                .orElseThrow(UnauthorizedException::wrong);
        return token;
    }
}
