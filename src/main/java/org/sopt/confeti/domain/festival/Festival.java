package org.sopt.confeti.domain.festival;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_favorite.FestivalFavorite;
import org.sopt.confeti.domain.festival_music.FestivalMusic;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.sopt.confeti.domain.timetable_festival.TimetableFestival;
import org.sopt.confeti.global.common.constant.Default;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "festivals")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 80, nullable = false)
    private String subtitle;

    @Column(nullable = false)
    private LocalDate startAt;

    @Column(nullable = false)
    private LocalDate endAt;

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

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalDate> dates = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalMusic> musics = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalReservationUrl> reservationUrls = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();

    @Builder
    private Festival(String title, String subtitle, LocalDate startAt, LocalDate endAt, String area,
                     String posterPath, String posterBgPath, String festivalInfoImgPath,
                     String logoPath, LocalDateTime reserveAt, String reservationUrl, String reservationOffice,
                     String ageRating, String time, String price, String address,
                     List<FestivalDate> dates, List<FestivalMusic> musics, List<FestivalReservationUrl> reservationUrls
    ) {
        this.title = title;
        this.subtitle = subtitle;
        this.startAt = startAt;
        this.endAt = endAt;
        this.area = area;
        this.posterPath = posterPath;
        this.posterBgPath = posterBgPath;
        this.festivalInfoImgPath = festivalInfoImgPath;
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
        this.reservationUrls = reservationUrls;

        this.dates.forEach(date -> date.setFestival(this));
        this.musics.forEach(music -> music.setFestival(this));
        this.reservationUrls.forEach(url -> url.setFestival(this));
    }

    public static Festival create(CreateFestivalDTO festivalDTO) {
        return Festival.builder()
                .title(festivalDTO.title())
                .subtitle(festivalDTO.subtitle())
                .startAt(festivalDTO.startAt())
                .endAt(festivalDTO.endAt())
                .area(festivalDTO.area())
                .posterPath(festivalDTO.posterPath())
                .posterBgPath(festivalDTO.posterBgPath())
                .festivalInfoImgPath(Default.IMG_PATH) // 제거 대상
                .logoPath(festivalDTO.logoPath())
                .reserveAt(festivalDTO.reserveAt())
                .reservationUrl(Default.URL) // 제거 대상
                .reservationOffice(Default.TEXT) // 제거 대상
                .ageRating(festivalDTO.ageRating())
                .time(festivalDTO.time())
                .price(festivalDTO.price())
                .address(festivalDTO.address())
                .dates(
                        festivalDTO.dates().stream()
                                .map(FestivalDate::create)
                                .toList()
                )
                .musics(
                        festivalDTO.musics().stream()
                                .map(FestivalMusic::create)
                                .toList()
                )
                .reservationUrls(
                        festivalDTO.reservationUrls().stream()
                                .map(FestivalReservationUrl::create)
                                .toList()
                )
                .build();
    }

    public void addDates(List<FestivalDate> dates) {
        this.dates.addAll(dates);
        dates.forEach(date -> date.setFestival(this));
    }

    public void addMusics(List<FestivalMusic> musics) {
        this.musics.addAll(musics);
        musics.forEach(music -> music.setFestival(this));
    }
}

