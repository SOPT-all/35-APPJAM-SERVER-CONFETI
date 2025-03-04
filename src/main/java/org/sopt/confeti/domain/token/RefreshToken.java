package org.sopt.confeti.domain.token;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private Long userId;

    private RefreshToken(String refreshToken, Long userId) {
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public static RefreshToken of(String refreshToken, Long userId) {
        return new RefreshToken(refreshToken, userId);
    }
}
