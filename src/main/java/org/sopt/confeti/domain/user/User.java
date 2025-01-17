package org.sopt.confeti.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(length=20, nullable = false)
    private String username;

    @Column(length=250, nullable = false)
    private String profilePath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true )
    private List<UserTimetable> timetables = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ArtistFavorite> artistFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<ConcertFavorite> concertFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true )
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();
}
