package org.sopt.confeti.domain.applemusic.song;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
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
@Table(name = "songs")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Song {

    @Id
    @Column(length = 20, nullable = false)
    private String id;

    @Column(length = 50, nullable = false)
    private String trackName;

    // width와 height 정보를 추가해야한다.
    @Column(length = 3000)
    private String artworkUrl;

    @Column(length = 3000)
    private String previewUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private Song(String songId, String trackName, String artworkUrl, String previewUrl) {
        this.id = songId;
        this.trackName = trackName;
        this.artworkUrl = artworkUrl;
        this.previewUrl = previewUrl;
    }

    public static Song create(String songId, String trackName, String artworkUrl,
        String previewUrl) {
        return Song.builder()
            .songId(songId)
            .trackName(trackName)
            .artworkUrl(artworkUrl)
            .previewUrl(previewUrl)
            .build();
    }
}
