package org.sopt.confeti.domain.festival_music;

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
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.resolver.artist.ConfetiMusic;

@Entity
@Table(name = "festival_musics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id")
    private Festival festival;

    @Embedded
    private ConfetiMusic music;

    @Builder
    public FestivalMusic(ConfetiMusic music) {
        this.music = music;
    }

    public static FestivalMusic create(final String musicId) {
        return FestivalMusic.builder()
                .music(ConfetiMusic.from(musicId))
                .build();
    }
}
