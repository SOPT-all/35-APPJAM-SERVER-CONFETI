package org.sopt.confeti.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.concertfavorite.ConcertFavorite;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.util.ArrayList;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=20, nullable = false)
    private String username;

    @Column(length=250, nullable = false)
    private String profilePath;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true )
    ArrayList<UserTimetable> timetables = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    ArrayList<ArtistFavorite> artistFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    ArrayList<ConcertFavorite> concertFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true )
    ArrayList<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true )
    ArrayList<TimetableFestival> timetableFestivals = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public ArrayList<UserTimetable> getTimetables() {
        return timetables;
    }

    public ArrayList<ArtistFavorite> getArtistFavorites() {
        return artistFavorites;
    }

    public ArrayList<ConcertFavorite> getConcertFavorites() {
        return concertFavorites;
    }

    public ArrayList<FestivalFavorite> getFestivalFavorites() {
        return festivalFavorites;
    }

    public ArrayList<TimetableFestival> getTimetableFestivals() {
        return timetableFestivals;
    }
}
