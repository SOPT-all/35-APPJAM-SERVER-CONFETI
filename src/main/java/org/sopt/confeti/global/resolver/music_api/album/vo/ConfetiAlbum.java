package org.sopt.confeti.global.resolver.music_api.album.vo;

import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistAlbumResponse;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiAlbum {

    private String id;

    @Setter
    private String name;

    @Setter
    private LocalDate releaseAt;

    public static ConfetiAlbum from(final AppleMusicArtistAlbumResponse albumResponse) {
        return new ConfetiAlbum(
                albumResponse.id(),
                "",
                null
        );
    }
}
