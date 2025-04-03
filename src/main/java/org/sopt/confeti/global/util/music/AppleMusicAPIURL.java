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

    @Value("${apple-music.api.endpoints.albums-path.single}")
    private String albumsPathSingle;

    @Value("${apple-music.api.endpoints.albums-path.multiple}")
    private String albumsPathMultiple;

    @Value("${apple-music.api.endpoints.albums-path.relationship-by-name}")
    private String albumsPathRelationshipByName;

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

    @Value("${apple-music.api.endpoints.search-path.suggestions}")
    private String searchPathSuggestions;


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

    public String getMultipleArtistsPath() {
        return getArtistsBasePath() + artistsPathMultiple;
    }

    public String getSingleAlbumPath(String id) {
        return UriComponentsBuilder.fromUriString(getAlbumsBasePath() + albumsPathSingle)
                .buildAndExpand(id)
                .toUriString();
    }

    public String getMultipleAlbumsPath() {
        return getAlbumsBasePath() + albumsPathMultiple;
    }

    public String getAlbumRelationshipByNamePath(String id, String relationship) {
        return UriComponentsBuilder.fromUriString(getAlbumsBasePath() + albumsPathRelationshipByName)
                .buildAndExpand(id, relationship)
                .toUriString();
    }

    public String getSingleSongPath(String id) {
        return UriComponentsBuilder.fromUriString(getSongsBasePath() + songsPathSingle)
                .buildAndExpand(id)
                .toUriString();
    }

    public String getMultipleSongsPath() {
        return getSongsBasePath() + songsPathMultiple;
    }

    public String getSingleSearchPath() {
        return getSearchBasePath() + searchPathSingle;
    }

    public String getSearchHintsPath() {
        return getSearchBasePath() + searchPathHints;
    }

    public String getSearchSuggestionsPath() {
        return getSearchBasePath() + searchPathSuggestions;
    }
}
