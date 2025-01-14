package org.sopt.confeti.domain.festivalstage;

import jakarta.persistence.*;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.domain.festivaldate.FestivalDate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_stages")
public class FestivalStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_date_id", nullable = false)
    private FestivalDate festivalDate;

    @Column(length = 30, nullable = false)
    private String name;

    @Column
    private int order;

    @OneToMany(mappedBy = "festival_stage", cascade = CascadeType.ALL, orphanRemoval = true)
    List<FestivalArtist> artists = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public FestivalDate getFestivalDate() {
        return festivalDate;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public List<FestivalArtist> getArtists() {
        return artists;
    }
}
