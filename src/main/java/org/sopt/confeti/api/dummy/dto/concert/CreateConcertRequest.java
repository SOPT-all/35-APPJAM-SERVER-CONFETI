package org.sopt.confeti.api.dummy.dto.concert;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CreateConcertRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String subtitle;
    @NotNull
    private LocalDateTime startAt;
    @NotNull
    private LocalDateTime endAt;
    @NotBlank
    private String area;
    @NotNull
    private LocalDateTime reserveAt;
    @NotBlank
    private String ageRating;
    @NotBlank
    private String time;
    @NotBlank
    private String price;
    @NotBlank
    private String address;

    @Valid
    @Size(min = 1)
    private List<CreateConcertArtistRequest> artists = new ArrayList<>();

    @Valid
    @Size(min = 1)
    private List<CreateConcertMusicRequest> musics = new ArrayList<>();

    @Valid
    @Size(min = 1)
    private List<CreateConcertReservationUrlRequest> reservationUrls = new ArrayList<>();
}
