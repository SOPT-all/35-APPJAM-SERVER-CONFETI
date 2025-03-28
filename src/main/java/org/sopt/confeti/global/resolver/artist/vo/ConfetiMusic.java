package org.sopt.confeti.global.resolver.artist.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiMusic {

    @Column(length = 50, nullable = false)
    private String musicId;

    @Transient
    private String title;

    private ConfetiMusic(String musicId) {
        this.musicId = musicId;
    }

    public static ConfetiMusic from(final String musicId) {
        return new ConfetiMusic(musicId);
    }
    public static ConfetiMusic empty() {
        return new ConfetiMusic();
    }
}
