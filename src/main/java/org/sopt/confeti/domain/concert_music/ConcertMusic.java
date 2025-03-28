package org.sopt.confeti.domain.concert_music;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertMusicDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiMusic;

@Entity
@Table(name = "concert_musics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private Concert concert;

    @Embedded
    private ConfetiMusic music;

    @Builder
    public ConcertMusic(ConfetiMusic music) {
        this.music = music;
    }

    public static ConcertMusic create(final String musicId) {
        return ConcertMusic.builder()
                .music(ConfetiMusic.from(musicId))
                .build();
    }

    public static ConcertMusic create(CreateConcertMusicDTO concertMusicDTO) {
        return ConcertMusic.builder()
                .music(ConfetiMusic.from(concertMusicDTO.musicId()))
                .build();
    }
}
