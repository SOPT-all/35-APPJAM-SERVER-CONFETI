package org.sopt.confeti.domain.festival;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="festivals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "festival_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String festivalTitle;

    @Column(length = 50, nullable = false)
    private String festivalSubtitle;

    @Column(nullable = false)
    private LocalDate festivalStartAt;

    @Column(nullable = false)
    private LocalDate festivalEndAt;

    @Column(length = 100, nullable = false)
    private String festivalArea;

    @Column(length = 250, nullable = false)
    private String festivalPosterPath;

    @Column(length = 250, nullable = false)
    private String festivalPosterBgPath;

    @Column(length = 250, nullable = false)
    private String festivalInfoImgPath;

    @Column(length = 250) // 나중에 nullable = false로 수정
    private String festivalReservationBgPath;

    @Column(length = 250)
    private String festivalLogoPath;

    @Column(nullable = false)
    private LocalDate reserveAt;

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

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalDate> dates = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();

    @Builder
    public Festival(String festivalTitle, String festivalSubtitle, LocalDate festivalStartAt,
                    LocalDate festivalEndAt, String festivalArea, String festivalPosterPath,
                    String festivalPosterBgPath,
                    String festivalInfoImgPath, String festivalReservationBgPath, String festivalLogoPath,
                    LocalDate reserveAt, String reservationUrl, String reservationOffice, String ageRating,
                    String time,
                    String price, List<FestivalDate> dates) {
        this.festivalTitle = festivalTitle;
        this.festivalSubtitle = festivalSubtitle;
        this.festivalStartAt = festivalStartAt;
        this.festivalEndAt = festivalEndAt;
        this.festivalArea = festivalArea;
        this.festivalPosterPath = festivalPosterPath;
        this.festivalPosterBgPath = festivalPosterBgPath;
        this.festivalInfoImgPath = festivalInfoImgPath;
        this.festivalReservationBgPath = festivalReservationBgPath;
        this.festivalLogoPath = festivalLogoPath;
        this.reserveAt = reserveAt;
        this.reservationUrl = reservationUrl;
        this.reservationOffice = reservationOffice;
        this.ageRating = ageRating;
        this.time = time;
        this.price = price;
        this.dates = dates;

        this.dates.forEach(date -> {
            date.setFestival(this);
        });
    }

    public static Festival create(CreateFestivalDTO createFestivalDTO) {
        return Festival.builder()
                .festivalTitle(createFestivalDTO.festivalTitle())
                .festivalSubtitle(createFestivalDTO.festivalSubtitle())
                .festivalStartAt(createFestivalDTO.festivalStartAt())
                .festivalEndAt(createFestivalDTO.festivalEndAt())
                .festivalArea(createFestivalDTO.festivalArea())
                .festivalPosterPath(createFestivalDTO.festivalPosterPath())
                .festivalPosterBgPath(createFestivalDTO.festivalPosterBgPath())
                .festivalInfoImgPath(createFestivalDTO.festivalInfoImgPath())
                .festivalReservationBgPath(createFestivalDTO.festivalReservationBgPath())
                .festivalLogoPath(createFestivalDTO.festivalLogoPath())
                .reserveAt(createFestivalDTO.reserveAt())
                .reservationUrl(createFestivalDTO.reservationUrl())
                .reservationOffice(createFestivalDTO.reservationOffice())
                .ageRating(createFestivalDTO.ageRating())
                .time(createFestivalDTO.time())
                .price(createFestivalDTO.price())
                .dates(
                        createFestivalDTO.dates().stream()
                                .map(FestivalDate::create)
                                .toList()
                )
                .build();
    }
}

