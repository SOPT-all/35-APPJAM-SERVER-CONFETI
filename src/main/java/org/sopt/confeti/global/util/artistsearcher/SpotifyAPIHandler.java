package org.sopt.confeti.global.util.artistsearcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;

@Component
public class SpotifyAPIHandler {

    @Value("${spotify.credentials.client-id}")
    private String clientId;

    @Value("${spotify.credentials.client-secret}")
    private String clientSecret;

    private SpotifyApi spotifyApi;

    private GetArtistRequest getArtistRequest;

    public SpotifyAPIHandler() {
        createSpotifyApi();
        generateAccessToken();
    }

    public List<ConfetiArtist> findArtistsByArtistIds(final List<String> artistIds)
            throws IOException, ParseException, SpotifyWebApiException {
        Artist[] artists = spotifyApi.getSeveralArtists(
                artistIds.toArray(new String[0])
        )
                .build()
                .execute();

        return Arrays.stream(artists)
                .map(ConfetiArtist::toConfetiArtist)
                .toList();
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
