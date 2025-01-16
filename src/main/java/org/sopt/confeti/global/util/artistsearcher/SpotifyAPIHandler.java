package org.sopt.confeti.global.util.artistsearcher;

import com.neovisionaries.i18n.CountryCode;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SpotifyAPIHandler {

    private static final int LIMIT = 1;
    private static final int OFFSET = 0;

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
        try {
            Paging<Artist> artists = spotifyApi.searchArtists(keyword)
                    .market(CountryCode.KR)
                    .limit(LIMIT)
                    .offset(OFFSET)
                    .build()
                    .execute();

            Optional<Artist> artist = Arrays.stream(artists.getItems())
                    .findFirst();

            return artist.map(ConfetiArtist::toConfetiArtist);

        } catch (IOException | ParseException | SpotifyWebApiException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ConfetiArtist> findArtistsByArtistIds(final List<String> artistIds) {
        try {
            Artist[] artists = spotifyApi.getSeveralArtists(
                            artistIds.toArray(new String[0])
                    )
                    .build()
                    .execute();

            return Arrays.stream(artists)
                    .map(ConfetiArtist::toConfetiArtist)
                    .toList();
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
