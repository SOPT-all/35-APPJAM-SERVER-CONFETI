package org.sopt.confeti.domain.applemusic.artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "artists", indexes = {
    @Index(name = "uk_artist_id", columnList = "artist_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artist_id", nullable = false)
    private String artistId;

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String artworkUrl;

    private Integer artworkWidth;

    private Integer artworkHeight;

    public static Artist create(String artistId, String name, String artworkUrl,
        Integer artworkWidth,
        Integer artworkHeight) {
        return Artist.builder()
            .artistId(artistId)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkWidth(artworkWidth)
            .artworkHeight(artworkHeight)
            .build();
    }

    @Builder
    private Artist(String artistId, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        this.artistId = artistId;
        this.name = name;
        this.artworkUrl = artworkUrl;
        this.artworkWidth = artworkWidth;
        this.artworkHeight = artworkHeight;
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
