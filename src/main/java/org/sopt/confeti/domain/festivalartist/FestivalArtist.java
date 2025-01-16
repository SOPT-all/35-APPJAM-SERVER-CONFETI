package org.sopt.confeti.domain.festivalartist;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festivalstage.FestivalStage;
import org.sopt.confeti.domain.festivaltime.FestivalTime;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="festival_artist")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "festival_artist_id")
    private Long id;

    @Column(length = 100, nullable = false)
    private String artistId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_time_id", nullable=false)
    private FestivalTime festivalTime;

    public Long getId() {
        return id;
    }

    public String getArtistId() {
        return artistId;
    }

    public FestivalTime getFestivalTime() {
        return festivalTime;
    }
}
