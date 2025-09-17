package org.sopt.confeti.domain.applemusic.artist_song;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.applemusic.artist.Artist;
import org.sopt.confeti.domain.applemusic.song.Song;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "artist_songs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "song_id")
    private Song song;

    public static ArtistSong create(Long id, Artist artist, Song song) {
        return ArtistSong.builder()
            .id(id)
            .artist(artist)
            .song(song)
            .build();
    }

    @Builder
    private ArtistSong(Long id, Artist artist, Song song) {
        this.id = id;
        this.artist = artist;
        this.song = song;
    }

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
