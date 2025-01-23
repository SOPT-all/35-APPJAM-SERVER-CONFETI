package org.sopt.confeti.global.util.artistsearcher;

import com.neovisionaries.i18n.CountryCode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Handler;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.DateConvertor;
import org.springframework.beans.factory.annotation.Value;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.BadRequestException;
import se.michaelthelin.spotify.exceptions.detailed.NotFoundException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

@Handler
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotifyAPIHandler {

    private static final int ARTIST_LIMIT = 1;
    private static final int ARTIST_OFFSET = 0;
    private static final int ALBUM_LIMIT = 1;
    private static final int ALBUM_OFFSET = 0;
    private static final int REFRESH_TRIAL = 5;
    private static final int REFRESH_INIT_VALUE = 0;

    @Value("${spotify.credentials.client-id}")
    private String clientId;

    @Value("${spotify.credentials.client-secret}")
    private String clientSecret;

    private SpotifyApi spotifyApi;

    private int refreshCount;

    public void init() {
        createSpotifyApi();
        generateAccessToken();

        refreshCount = REFRESH_INIT_VALUE;
    }

    public Optional<ConfetiArtist> findArtistsByKeyword(final String keyword) {
        Optional<Artist> searched = searchArtistByKeyword(keyword);

        if (searched.isEmpty()) {
            return Optional.empty();
        }

        Artist artist = searched.get();

        return Optional.of(
                ConfetiArtist.toConfetiArtist(artist, findLatestReleaseAt(artist.getId()))
        );
    }

    public Optional<ConfetiArtist> findArtistByArtistId(final String artistId) {
        if (artistId == null || artistId.isBlank()) {
            return Optional.empty();
        }

        try {
            generateAccessTokenIfExpired();

            return executeWithTokenRefresh(() -> {
                Artist artist = spotifyApi.getArtist(artistId)
                        .build()
                        .execute();

                return Optional.of(
                        ConfetiArtist.toConfetiArtist(artist)
                );
            });
        } catch (NotFoundException e) {
            return Optional.empty();
        } catch (BadRequestException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    public List<ConfetiArtist> findArtistsByArtistIds(final List<String> artistIds) {
        if (artistIds.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            generateAccessTokenIfExpired();

            return executeWithTokenRefresh(() -> {
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
            });
        } catch (BadRequestException e) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private Optional<Artist> searchArtistByKeyword(final String keyword) {
        try {
            generateAccessTokenIfExpired();

            return executeWithTokenRefresh(() -> {
                Paging<Artist> artists = spotifyApi.searchArtists(keyword)
                        .market(CountryCode.KR)
                        .limit(ARTIST_LIMIT)
                        .offset(ARTIST_OFFSET)
                        .build()
                        .execute();

                return Arrays.stream(artists.getItems())
                        .findFirst();
            });
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private LocalDate findLatestReleaseAt(final String artistId) {
        Optional<Paging<AlbumSimplified>> searchedAlbums = searchAlbumByArtistId(artistId);

        if (searchedAlbums.isEmpty()) {
            return null;
        }

        Paging<AlbumSimplified> albums = searchedAlbums.get();

        return Arrays.stream(albums.getItems())
                .map(albumItem -> DateConvertor.convertToSpotifyLocalDate(albumItem.getReleaseDate()))
                .findFirst()
                .orElseGet(null);
    }

    private Optional<Paging<AlbumSimplified>> searchAlbumByArtistId(final String artistId) {
        try {
            generateAccessTokenIfExpired();

            return executeWithTokenRefresh(() ->
                    Optional.of(spotifyApi.getArtistsAlbums(artistId)
                    .market(CountryCode.KR)
                    .limit(ALBUM_LIMIT)
                    .offset(ALBUM_OFFSET)
                    .build()
                    .execute()));
        } catch (NotFoundException | BadRequestException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private void createSpotifyApi() {
        this.spotifyApi = SpotifyApi.builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .build();
    }

    private void generateAccessToken() {
        try {
            final ClientCredentials clientCredentials = spotifyApi.clientCredentials()
                    .build()
                    .execute();

            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            refreshCount = REFRESH_INIT_VALUE;

        } catch (Exception e) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }
    }

    private void generateAccessTokenIfExpired() {
        if (spotifyApi.getAccessToken() == null) {
            generateAccessToken();
        }
    }

    // 토큰 재발급을 위한 실행 메서드
    private <T> T executeWithTokenRefresh(Callable<T> action) throws Exception {
        try {
            T retVal = action.call();
            refreshCount = REFRESH_INIT_VALUE;
            return retVal;
        } catch (UnauthorizedException e) {
            if (isTokenExpired(e) && refreshCount < REFRESH_TRIAL) {
                generateAccessToken();
                return action.call();  // 새 토큰으로 재시도
            }
            throw e;
        }
    }

    // 토큰 만료 여부 확인
    private boolean isTokenExpired(SpotifyWebApiException e) {
        return e instanceof UnauthorizedException;
    }
}
