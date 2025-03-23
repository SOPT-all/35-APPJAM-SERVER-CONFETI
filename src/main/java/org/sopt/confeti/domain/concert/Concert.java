package org.sopt.confeti.domain.concert;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.performance.facade.dto.request.CreateConcertDTO;
import org.sopt.confeti.domain.concert_music.ConcertMusic;
import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.sopt.confeti.domain.concertartist.ConcertArtist;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="concerts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(length = 100, nullable = false)
    private String area;

    @Setter
    @Column(length = 250, nullable = false)
    private String posterPath;

    @Setter
    @Column(length = 250, nullable = false)
    private String posterBgPath;

    @Setter
    @Column(length = 250, nullable = false)
    private String concertInfoImgPath; // 제거 대상

    @Setter
    @Column(length = 250) // 나중에 nullable = false로 수정
    private String concertReservationBgPath; // 제거 대상

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @Column(length = 250, nullable = false)
    private String reservationUrl; // 제거 대상

    @Column(length = 50, nullable = false)
    private String reservationOffice; // 제거 대상

    @Column(length = 30, nullable = false)
    private String ageRating;

    @Column(name = "times", length = 30, nullable = false)
    private String time;

    @Column(length = 200, nullable = false)
    private String price;

    @Column(length = 100, nullable = false)
    private String address;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertArtist> artists = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertMusic> musics = new ArrayList<>();

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConcertReservationUrl> reservationUrls = new ArrayList<>();

    @Builder
    public Concert(String title, String subtitle, LocalDateTime startAt, LocalDateTime endAt, String area,
                   String posterPath, String posterBgPath, String concertInfoImgPath, String concertReservationBgPath,
                   LocalDateTime reserveAt, String reservationUrl, String reservationOffice, String ageRating,
                   String time, String price, String address,
                   List<ConcertArtist> artists, List<ConcertMusic> musics, List<ConcertReservationUrl> reservationUrls
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.posterBgPath = posterBgPath;
        this.concertInfoImgPath = concertInfoImgPath;
        this.concertReservationBgPath = concertReservationBgPath;
        this.reserveAt = reserveAt;
        this.reservationUrl = reservationUrl;
        this.reservationOffice = reservationOffice;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.artists = artists;
        this.musics = musics;
        this.reservationUrls = reservationUrls;

        artists.forEach(artist -> artist.setConcert(this));
        musics.forEach(music -> music.setConcert(this));
        reservationUrls.forEach(url -> url.setConcert(this));
    }

    public static Concert create(final CreateConcertDTO from) {
        return Concert.builder()
                .title(from.concertTitle())
                .subtitle(from.concertSubtitle())
                .startAt(from.concertStartAt())
                .endAt(from.concertEndAt())
                .area(from.concertArea())
                .posterPath("")
                .posterBgPath("")
                .concertInfoImgPath("")
                .concertReservationBgPath("")
                .reserveAt(from.reserveAt())
                .reservationUrl(from.reservationUrl())
                .reservationOffice(from.reservationOffice())
                .ageRating(from.ageRating())
                .time(from.time())
                .price(from.price())
                .artists(
                        from.concertArtists().stream()
                                .map(ConcertArtist::create)
                                .toList()
                )
                // TODO : 콘서트 생성 로직에 음악 넣기
                .build();
    }
}
