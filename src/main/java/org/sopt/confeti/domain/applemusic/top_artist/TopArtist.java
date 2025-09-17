package org.sopt.confeti.domain.applemusic.top_artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.applemusic.artist.Artist;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "top_artists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TopArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", unique = true)
    private Artist artist;

    @Column(nullable = false)
    private int rank;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private TopArtist(Long id, Artist artist, int rank) {
        this.id = id;
        this.artist = artist;
        this.rank = rank;
    }

    public static TopArtist create(Artist artist, int rank) {
        return TopArtist.builder()
            .artist(artist)
            .rank(rank)
            .build();
    }
}
