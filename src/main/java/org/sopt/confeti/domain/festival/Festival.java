package org.sopt.confeti.domain.festival;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festival_music.FestivalMusic;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festivals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival {

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
    private String festivalInfoImgPath; // 제거 대상

    @Setter
    @Column(length = 250) // 나중에 nullable = false로 수정
    private String festivalReservationBgPath; // 제거 대상

    @Setter
    @Column(length = 250)
    private String logoPath;

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

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalDate> dates = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalMusic> musics = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();

    @Builder
    public Festival(String title, String subtitle, LocalDateTime startAt, LocalDateTime endAt, String area,
                    String posterPath, String posterBgPath, String festivalInfoImgPath,
                    String festivalReservationBgPath,
                    String logoPath, LocalDateTime reserveAt, String reservationUrl, String reservationOffice,
                    String ageRating, String time, String price, String address,
                    List<FestivalDate> dates, List<FestivalMusic> musics
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.posterBgPath = posterBgPath;
        this.festivalInfoImgPath = festivalInfoImgPath;
        this.festivalReservationBgPath = festivalReservationBgPath;
        this.logoPath = logoPath;
        this.reserveAt = reserveAt;
        this.reservationUrl = reservationUrl;
        this.reservationOffice = reservationOffice;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.address = address;
        this.dates = dates;
        this.musics = musics;
    }

    public static Festival create(CreateFestivalDTO createFestivalDTO) {
        return Festival.builder()
                .title(createFestivalDTO.festivalTitle())
                .subtitle(createFestivalDTO.festivalSubtitle())
                .startAt(createFestivalDTO.festivalStartAt())
                .endAt(createFestivalDTO.festivalEndAt())
                .area(createFestivalDTO.festivalArea())
                .posterPath("")
                .posterBgPath("")
                .festivalInfoImgPath("")
                .festivalReservationBgPath("")
                .logoPath("")
                .reserveAt(createFestivalDTO.reserveAt())
                .reservationUrl(createFestivalDTO.reservationUrl())
                .reservationOffice(createFestivalDTO.reservationOffice())
                .ageRating(createFestivalDTO.ageRating())
                .time(createFestivalDTO.time())
                .price(createFestivalDTO.price())
                // TODO : 페스티벌 생성 시 주소 값 추가
                .dates(
                        createFestivalDTO.dates().stream()
                                .map(FestivalDate::create)
                                .toList()
                )
                .build();
    }
}

