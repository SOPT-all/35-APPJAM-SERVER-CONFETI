package org.sopt.confeti.global.util.music;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AppleMusicAPIURL {

    @Getter
    @Value("${apple-music.api.endpoints.base-url}")
    private String baseUrl;

    @Value("${apple-music.api.endpoints.path-prefix}")
    private String pathPrefix;

    @Value("${apple-music.api.endpoints.artists-path.base}")
    private String artistsPathBase;

    @Value("${apple-music.api.endpoints.artists-path.single}")
    private String artistsPathSingle;

    @Value("${apple-music.api.endpoints.artists-path.multiple}")
    private String artistsPathMultiple;

    @Value("${apple-music.api.endpoints.albums-path.base}")
    private String albumsPathBase;

    @Value("${apple-music.api.endpoints.albums-path.multiple}")
    private String albumsPathMultiple;

    @Value("${apple-music.api.endpoints.songs-path.base}")
    private String songsPathBase;

    @Value("${apple-music.api.endpoints.songs-path.single}")
    private String songsPathSingle;

    @Value("${apple-music.api.endpoints.songs-path.multiple}")
    private String songsPathMultiple;

    @Value("${apple-music.api.endpoints.search-path.base}")
    private String searchPathBase;

    @Value("${apple-music.api.endpoints.search-path.single}")
    private String searchPathSingle;

    @Value("${apple-music.api.endpoints.search-path.hints}")
    private String searchPathHints;

    private String getArtistsBasePath() {
        return pathPrefix + artistsPathBase;
    }

    private String getAlbumsBasePath() {
        return pathPrefix + albumsPathBase;
    }

    private String getSongsBasePath() {
        return pathPrefix + songsPathBase;
    }

    private String getSearchBasePath() {
        return pathPrefix + searchPathBase;
    }

    public String getSingleArtistPath(String id) {
        return UriComponentsBuilder.fromUriString(getArtistsBasePath() + artistsPathSingle)
                .buildAndExpand(id)
                .toUriString();
    }

    public String getMultipleArtistsUrl() {
        return getArtistsBasePath() + artistsPathMultiple;
    }

    public String getMultipleAlbumsUrl() {
        return getAlbumsBasePath() + albumsPathMultiple;
    }

    public String getSingleSongUrl(String id) {
        return UriComponentsBuilder.fromUriString(getSongsBasePath() + songsPathSingle)
                .buildAndExpand(id)
                .toUriString();
    }

    public String getMultipleSongsUrl() {
        return getSongsBasePath() + songsPathMultiple;
    }

    public String getSingleSearchUrl() {
        return getSearchBasePath() + searchPathSingle;
    }

    public String getHintsSearchUrl() {
        return getSearchBasePath() + searchPathHints;
    }
}
