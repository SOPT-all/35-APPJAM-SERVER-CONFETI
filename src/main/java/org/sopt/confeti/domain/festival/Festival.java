package org.sopt.confeti.domain.festival;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private List<FestivalDate> dates= new ArrayList<>();

    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<FestivalFavorite> festivalFavorites = new ArrayList<>();


    @OneToMany(mappedBy = "festival", cascade = CascadeType.REMOVE)
    private List<TimetableFestival> timetableFestivals = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<FestivalDate> getDates() {
        return dates;
    }

    public List<FestivalFavorite> getFestivalFavorites() {
        return festivalFavorites;
    }

    public List<TimetableFestival> getTimetableFestivals() {
        return timetableFestivals;
    }

    public String getFestivalTitle() {
        return festivalTitle;
    }

    public LocalDate getFestivalStartAt() {
        return festivalStartAt;
    }

    public LocalDate getFestivalEndAt() {
        return festivalEndAt;
    }

    public String getFestivalArea() {
        return festivalArea;
    }

    public String getFestivalPosterPath() {
        return festivalPosterPath;
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

    public String getFestivalSubtitle() {
        return festivalSubtitle;
    }

    public String getFestivalPosterBgPath() {
        return festivalPosterBgPath;
    }

    public String getFestivalInfoImgPath() {
        return festivalInfoImgPath;
    }

    public String getFestivalReservationBgPath() {
        return festivalReservationBgPath;
    }
}

