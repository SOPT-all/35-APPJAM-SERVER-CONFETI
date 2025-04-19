package org.sopt.confeti.global.resolver.music_api.music.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicArtistResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiMusicArtist {

    private String id;

    public static ConfetiMusicArtist from(final AppleMusicMusicArtistResponse artist) {
        return new ConfetiMusicArtist(
                artist.id()
        );
    }
}
