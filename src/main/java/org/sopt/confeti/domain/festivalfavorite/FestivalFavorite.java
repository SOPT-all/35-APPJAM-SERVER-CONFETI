package org.sopt.confeti.domain.festivalfavorite;

import jakarta.persistence.*;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="festival_favorites")
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
}
