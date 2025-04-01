package org.sopt.confeti.domain.festival_favorite;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name = "festival_favorites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

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
