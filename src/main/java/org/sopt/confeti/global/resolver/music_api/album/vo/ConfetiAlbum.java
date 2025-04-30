package org.sopt.confeti.global.resolver.music_api.album.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.global.util.music.dto.album.AppleMusicAlbumResponse;
import org.sopt.confeti.global.util.music.dto.artist.AppleMusicArtistAlbumResponse;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfetiAlbum {

    private String id;

    @Setter
    private String name;

    public static ConfetiAlbum from(final AppleMusicArtistAlbumResponse album) {
        return new ConfetiAlbum(
                album.id(),
                ""
        );
    }

    public static ConfetiAlbum from(final String albumId) {
        return new ConfetiAlbum(
                albumId,
                ""
        );
    }

    public static ConfetiAlbum from(final AppleMusicAlbumResponse album) {
        return new ConfetiAlbum(
                album.id(),
                album.attributes().name()
        );
    }

    public static ConfetiAlbum empty() {
        return new ConfetiAlbum();
    }
}
