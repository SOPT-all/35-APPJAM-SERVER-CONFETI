package org.sopt.confeti.domain.applemusic.song;

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
@Table(name = "songs", indexes = {
    @Index(name = "uk_song_id", columnList = "song_id", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "song_id", nullable = false)
    private String songId;

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String artworkUrl;

    private Integer artworkWidth;

    private Integer artworkHeight;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Song create(String songId, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        return Song.builder()
            .songId(songId)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkHeight(artworkHeight)
            .artworkWidth(artworkWidth)
            .build();
    }

    @Builder
    private Song(String songId, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        this.songId = songId;
        this.name = name;
        this.artworkUrl = artworkUrl;
        this.artworkWidth = artworkWidth;
        this.artworkHeight = artworkHeight;
    }
}
