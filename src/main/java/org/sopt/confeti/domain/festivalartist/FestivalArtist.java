package org.sopt.confeti.domain.festivalartist;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festivalstage.FestivalStage;
import org.sopt.confeti.domain.festivaltime.FestivalTime;
import org.sopt.confeti.domain.usertimetable.UserTimetable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

@Entity
@Table(name="festival_artist")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalArtist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "festival_artist_id")
    private Long id;

    @Embedded
    private ConfetiArtist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_time_id", nullable=false)
    private FestivalTime festivalTime;
}
