package org.sopt.confeti.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;

import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.domain.user.constant.Role;

@Entity
@Table(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OAuthProvider provider;

    @Column(length = 20,nullable = false)
    private String socialId;

    @Column(length=20, nullable = false)
    private String username;

    @Column(length=250, nullable = false)
    private String profilePath;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ArtistFavorite> artistFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ConcertFavorite> concertFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true )
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();

    public static User create(AuthUser authUser) {
        return User.builder()
                .provider(authUser.getProvider())
                .socialId(authUser.getSocialId())
                .username(authUser.getSocialNickname())
                .profilePath(authUser.getSocialProfile())
                .role(authUser.getRole())
                .build();
    }

    public AuthUser toAuthUser() {
        return AuthUser.createWithId(id, provider, socialId, username, profilePath, role);
    }

    @Builder
    public User(OAuthProvider provider, String socialId, String username, String profilePath, Role role) {
        this.provider = provider;
        this.socialId = socialId;
        this.username = username;
        this.profilePath = profilePath;
        this.role = role;
    }
}
