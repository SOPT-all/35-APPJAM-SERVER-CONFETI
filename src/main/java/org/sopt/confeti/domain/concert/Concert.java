package org.sopt.confeti.domain.concert;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.concertartist.ConcertArtist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

@Entity
@Table(name="concerts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="concert_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String concertTitle;

    @Column(length = 50, nullable = false)
    private String concertSubtitle;

    @Column(nullable = false)
    private LocalDateTime concertStartAt;

    @Column(nullable = false)
    private LocalDateTime concertEndAt;

    @Column(length = 100, nullable = false)
    private String concertArea;

    @Column(length = 250, nullable = false)
    private String concertPosterPath;

    @Column(length = 250, nullable = false)
    private String concertPosterBgPath;

    @Column(length = 250, nullable = false)
    private String concertInfoImgPath;

    @Column(length = 250) // 나중에 nullable = false로 수정
    private String concertReservationBgPath;

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @Column(length = 250, nullable = false)
    private String reservationUrl;

    @Column(length = 50, nullable = false)
    private String reservationOffice;

    @Column(length = 30, nullable = false)
    private String ageRating;

    @Column(name = "times", length = 30, nullable = false)
    private String time;

    @Column(length = 200, nullable = false)
    private String price;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.REMOVE)
    private List<ConcertArtist> artists = new ArrayList<>();
}
