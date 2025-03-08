package org.sopt.confeti.domain.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.auth.dto.OAuthSocialInfoResult;

@Entity
@Getter
@Table(name = "apple_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String tokenId;

    @Column(nullable = false)
    private long userId;

    @Setter
    @Column(length = 100, nullable = false)
    private String accessToken;

    @Setter
    @Column(length = 100, nullable = false)
    private String refreshToken;

    @Setter
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Builder
    public AppleToken(long userId, String accessToken, String refreshToken, LocalDateTime expiresAt) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public static AppleToken create(long userId, OAuthSocialInfoResult socialInfo) {
        return AppleToken.builder()
                .userId(userId)
                .accessToken(socialInfo.token().accessToken())
                .refreshToken(socialInfo.token().refreshToken())
                .expiresAt(LocalDateTime.now().plusSeconds(socialInfo.token().expiresIn()))
                .build();
    }
}
