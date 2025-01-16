package org.sopt.confeti.domain.festivalstage;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_date_id", nullable = false)
    private FestivalDate festivalDate;

    @Column(length = 30, nullable = false)
    private String name;

    @Column(name = "orders", nullable = false)
    private int order;

    @OneToMany(mappedBy = "festivalStage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FestivalTime> times = new ArrayList<>();
}
