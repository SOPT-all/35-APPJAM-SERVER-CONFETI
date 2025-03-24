package org.sopt.confeti.domain.festival_date;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival_stage.FestivalStage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_dates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false)
    private Festival festival;

    @Column(nullable = false)
    private LocalDate festivalAt;

    @Column(nullable = false)
    private LocalTime openAt;

    @OneToMany(mappedBy = "festivalDate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalStage> stages = new ArrayList<>();

    @Builder
    public FestivalDate(LocalDate festivalAt, LocalTime openAt,
                        List<FestivalStage> stages) {
        this.festivalAt = festivalAt;
        this.openAt = openAt;
        this.stages = stages;

        this.stages.forEach(stage -> {
            stage.setFestivalDate(this);
        });
    }
}
