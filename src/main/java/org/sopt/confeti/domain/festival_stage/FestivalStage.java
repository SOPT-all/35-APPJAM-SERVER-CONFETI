package org.sopt.confeti.domain.festival_stage;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalStageDTO;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_time.FestivalTime;

@Entity
@Table(name = "festival_stages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_date_id", nullable = false)
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

    public void update(String name, int order) {
        this.name = name;
        this.order = order;
    }

    public void addTime(FestivalTime time) {
        this.times.add(time);
        time.setFestivalStage(this);
    }

    public static FestivalStage create(CreateFestivalStageDTO festivalStageDTO) {
        return FestivalStage.builder()
                .name(festivalStageDTO.name())
                .order(festivalStageDTO.order())
                .times(
                        festivalStageDTO.times().stream()
                                .map(FestivalTime::create)
                                .toList()
                )
                .build();
    }

    public static FestivalStage create(String name, int order, List<FestivalTime> times) {
        return FestivalStage.builder()
                .name(name)
                .order(order)
                .times(times)
                .build();
    }
}
