package org.sopt.confeti.domain.user;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.concert_favorite.ConcertFavorite;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.domain.user.constant.Role;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private OAuthProvider provider;

    @Column(length = 100, nullable = false)
    private String socialId;

    @Setter
    @Column(length = 30, nullable = false)
    private String name;

    @Setter
    @Column(length = 250)
    private String profilePath;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Role role;

    @Setter
    @Column(nullable = false)
    private boolean hasTimetableHistory;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ArtistFavorite> artistFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ConcertFavorite> concertFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Setlist> setlists = new ArrayList<>();

    @Builder
    public User(OAuthProvider provider, String socialId, String name, String profilePath,
        Role role) {
        this.provider = provider;
        this.socialId = socialId;
        this.name = name;
        this.profilePath = profilePath;
        this.role = role;
        this.hasTimetableHistory = false;
    }

    public static User create(AuthUser authUser) {
        return User.builder()
            .provider(authUser.getProvider())
            .socialId(authUser.getSocialId())
            .name(authUser.getSocialNickname())
            .profilePath(authUser.getSocialProfile())
            .role(authUser.getRole())
            .build();
    }

    public AuthUser toAuthUser() {
        return AuthUser.createWithId(id, provider, socialId, name, profilePath, role);
    }

    public UserInfo toUserInfo() {
        return UserInfo.builder()
            .id(id)
            .provider(provider)
            .socialId(socialId)
            .name(name)
            .profilePath(profilePath)
            .role(role)
            .hasTimetableHistory(hasTimetableHistory)
            .build();
    }
}
