package org.sopt.confeti.global.util.artistsearcher;

import com.neovisionaries.i18n.CountryCode;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.sopt.confeti.annotation.Handler;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.DateConvertor;
import org.springframework.beans.factory.annotation.Value;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Handler
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotifyAPIHandler {

    private static final int ARTIST_LIMIT = 1;
    private static final int ARTIST_OFFSET = 0;
    private static final int ALBUM_LIMIT = 10;
    private static final int ALBUM_OFFSET = 0;

    @Value("${spotify.credentials.client-id}")
    private String clientId;

    @Value("${spotify.credentials.client-secret}")
    private String clientSecret;

    private SpotifyApi spotifyApi;

    public void init() {
        createSpotifyApi();
        generateAccessToken();
    }

    public Optional<ConfetiArtist> findArtistsByKeyword(final String keyword) {
        Optional<Artist> searchedArtist = searchArtistByKeyword(keyword);

        if (searchedArtist.isEmpty()) {
            return Optional.empty();
        }

        Artist artist = searchedArtist.get();

        return Optional.of(
                ConfetiArtist.toConfetiArtist(artist, findLatestReleaseAt(keyword, artist))
        );
    }

    public Optional<ConfetiArtist> findArtistByArtistId(final String artistId) {
        if (artistId == null || artistId.isBlank()) {
            return Optional.empty();
        }

        try {
            Artist artist = spotifyApi.getArtist(artistId)
                    .build()
                    .execute();

            return Optional.of(
                    ConfetiArtist.toConfetiArtist(artist)
            );
        } catch (NotFoundException e) {
            return Optional.empty();
        } catch (BadRequestException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ConfetiArtist> findArtistsByArtistIds(final List<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            Artist[] artists = spotifyApi.getSeveralArtists(
                        artistIds.stream()
                                .filter(Objects::nonNull)
                                .toArray(String[]::new)
                    )
                    .build()
                    .execute();

            return Arrays.stream(artists)
                    .filter(Objects::nonNull)
                    .map(ConfetiArtist::toConfetiArtist)
                    .toList();
        } catch (BadRequestException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Artist> searchArtistByKeyword(final String keyword) {
        try {
            Paging<Artist> artists = spotifyApi.searchArtists(keyword)
                    .market(CountryCode.KR)
                    .limit(ARTIST_LIMIT)
                    .offset(ARTIST_OFFSET)
                    .build()
                    .execute();

            return Arrays.stream(artists.getItems())
                    .findFirst();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }
    }

    private LocalDate findLatestReleaseAt(final String keyword, final Artist artist) {
        Paging<AlbumSimplified> albums = searchAlbumByKeyword(keyword);

        return Arrays.stream(albums.getItems())
                .filter((searchedAlbum) ->
                        Arrays.stream(searchedAlbum.getArtists())
                                .anyMatch((searchedArtist) ->
                                        searchedArtist.getId().equals(artist.getId())
                                )
                )
                .max(Comparator.comparing(AlbumSimplified::getReleaseDate))
                .map(
                        albumSimplified -> DateConvertor.convertToSpotifyLocalDate(albumSimplified.getReleaseDate())
                )
                .get();
    }

    private Paging<AlbumSimplified> searchAlbumByKeyword(final String keyword) {
        try {
            return spotifyApi.searchAlbums(keyword)
                    .market(CountryCode.KR)
                    .limit(ALBUM_LIMIT)
                    .offset(ALBUM_OFFSET)
                    .build()
                    .execute();
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void createSpotifyApi() {
        this.spotifyApi = SpotifyApi.builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    private void generateAccessToken() {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();

        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }

}
