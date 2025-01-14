package org.sopt.confeti.domain.festivalfavorite;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="festival_favorites")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="festival_favorite_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_id")
    private Festival festival;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Festival getFestival() {
        return festival;
    }

    @Builder
    public FestivalFavorite(User user, Festival festival) {
        this.user = user;
        this.festival = festival;
    }

    public static FestivalFavorite create(User user, Festival festival) {
        return FestivalFavorite.builder()
                .user(user)
                .festival(festival)
                .build();
    }
}
