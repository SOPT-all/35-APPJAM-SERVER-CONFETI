package org.sopt.confeti.domain.festival_artist;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalArtistDTO;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_time.FestivalTime;
import org.sopt.confeti.domain.music.artist.Artist;

@Entity
@Table(name = "festival_artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FestivalArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_time_id", nullable = true)
    private FestivalTime festivalTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_date_id", nullable = false)
    private FestivalDate festivalDate;

    @Builder
    public FestivalArtist(Artist artist) {
        this.artist = artist;
    }

    public static FestivalArtist create(CreateFestivalArtistDTO festivalArtistDTO) {
        return FestivalArtist.builder()
            .artist(Artist.create(festivalArtistDTO.artistId()))
            .build();
    }

    public static FestivalArtist create(String artistId) {
        return FestivalArtist.builder()
            .artist(Artist.create(artistId))
            .build();
    }
}
