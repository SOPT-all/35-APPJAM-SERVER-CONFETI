package org.sopt.confeti.global.resolver.music_api.song.vo;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.util.music.dto.music.AppleMusicMusicArtistResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiSongArtist {

    private String id;

    public static ConfetiSongArtist from(final AppleMusicMusicArtistResponse artist) {
        Optional<AppleMusicMusicArtistResponse> optArtist = Optional.ofNullable(artist);
        String id = optArtist.map(AppleMusicMusicArtistResponse::id).orElse(null);

        return new ConfetiSongArtist(
            id
        );
    }
}
