package org.sopt.confeti.domain.festival;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.domain.festivalfavorite.FestivalFavorite;
import org.sopt.confeti.domain.timetablefestival.TimetableFestival;

import java.time.LocalDate;
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

    @Column(length = 30, nullable = false)
    private String festivalArea;

    @Column(length = 250, nullable = false)
    private String festivalPosterPath;

    @Column(length = 250, nullable = false)
    private String festivalPosterBgPath;

    @Column(length = 250, nullable = false)
    private String festivalInfoImgPath;

    @Column(length = 250)
    private String festivalReservationBgPath;

    @Column(length = 250)
    private String festivalLogoPath;

    @Column(nullable = false)
    private LocalDateTime reserveAt;

    @Column(length = 250, nullable = false)
    private String reservationUrl;

    @Column(length = 30, nullable = false)
    private String reservationOffice;

    @Column(length = 30, nullable = false)
    private String ageRating;

    @Column(name = "times", length = 30, nullable = false)
    private String time;

    @Column(length = 100, nullable = false)
    private String price;

    @OneToMany(mappedBy = "festival", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalDate> dates = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();
}

