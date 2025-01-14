package org.sopt.confeti.domain.festivaldate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festivalstage.FestivalStage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_dates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalDate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="festival_date_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Column(nullable = false)
    private LocalDate festivalAt;

    @Column(nullable = false)
    private LocalTime openAt;

    @OneToMany(mappedBy = "festivalDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalStage> stages = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Festival getFestival() {
        return festival;
    }

    public LocalDate getFestivalAt() {
        return festivalAt;
    }

    public LocalTime getOpenAt() {
        return openAt;
    }

    public List<FestivalStage> getStages() {
        return stages;
    }
}
