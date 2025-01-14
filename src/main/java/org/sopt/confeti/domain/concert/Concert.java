package org.sopt.confeti.domain.concert;

import jakarta.persistence.*;
import org.sopt.confeti.domain.concertartist.ConcertArtist;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="concerts")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="concert_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String concertTitle;

    @Column(nullable = false)
    private LocalDateTime concertStartAt;

    @Column(nullable = false)
    private LocalDateTime concertEndAt;

    @Column(length = 30, nullable = false)
    private String concertArea;

    @Column(length = 250, nullable = false)
    private String concertPosterPath;

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @Column(length = 250, nullable = false)
    private String reservationUrl;

    @Column(length = 30, nullable = false)
    private String reservationOffice;

    @Column(length = 30, nullable = false)
    private String ageRating;

    @Column(length = 30, nullable = false)
    private String time;

    @Column(length = 100, nullable = false)
    private String price;

    @Column(length = 250, nullable = false)
    private String infoImgUrl;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.REMOVE)
    private List<ConcertArtist> artists = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getConcertTitle() {
        return concertTitle;
    }

    public LocalDateTime getConcertStartAt() {
        return concertStartAt;
    }

    public LocalDateTime getConcertEndAt() {
        return concertEndAt;
    }

    public String getConcertArea() {
        return concertArea;
    }

    public String getConcertPosterPath() {
        return concertPosterPath;
    }

    public LocalDateTime getReserveAt() {
        return reserveAt;
    }

    public String getReservationUrl() {
        return reservationUrl;
    }

    public String getReservationOffice() {
        return reservationOffice;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getInfoImgUrl() {
        return infoImgUrl;
    }

    public List<ConcertArtist> getArtists() {
        return artists;
    }
}
