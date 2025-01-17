package org.sopt.confeti.domain.festivalstage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalStageDTO;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.domain.festivaltime.FestivalTime;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_stages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="festival_stage_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_date_id", nullable = false)
    private FestivalDate festivalDate;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(name = "orders", nullable = false)
    private int order;

    @OneToMany(mappedBy = "festivalStage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalTime> times = new ArrayList<>();

    @Builder
    public FestivalStage(String name, int order, List<FestivalTime> times) {
        this.name = name;
        this.order = order;
        this.times = times;

        this.times.forEach(time -> {
            time.setFestivalStage(this);
        });
    }

    public static FestivalStage create(final CreateFestivalStageDTO createFestivalStageDTO) {
        return FestivalStage.builder()
                .name(createFestivalStageDTO.name())
                .order(createFestivalStageDTO.orders())
                .times(
                        createFestivalStageDTO.times().stream()
                                .map(FestivalTime::create)
                                .toList()
                )
                .build();
    }
}
