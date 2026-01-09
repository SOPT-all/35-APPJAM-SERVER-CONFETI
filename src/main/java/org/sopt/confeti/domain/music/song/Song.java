package org.sopt.confeti.domain.music.song;

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
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
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
    @Column(nullable = false)
    private String id;

    @Column(length = 1000, nullable = false)
    private String trackName;

    @Column(length = 1000)
    private String artistName;

    /*
     * Apple Music에서 width와 height를 커스텀할 수 있도록 아래의 형식으로 온다.
     * ex) https://~~{w}-{y}.png
     * 필요에 맞춰 width, height 를 넣어야한다.
     */
    @Column(length = 3000)
    private String artworkUrl;

    @Column(length = 3000)
    private String previewUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    private Song(String songId,
        String trackName,
        String artistName,
        String artworkUrl,
        String previewUrl) {
        this.id = songId;
        this.trackName = trackName;
        this.artistName = artistName;
        this.artworkUrl = artworkUrl;
        this.previewUrl = previewUrl;
    }

    public static Song create(String songId,
        String trackName,
        String artistName,
        String artworkUrl,
        String previewUrl) {
        return Song.builder()
            .songId(songId)
            .trackName(trackName)
            .artistName(artistName)
            .artworkUrl(artworkUrl)
            .previewUrl(previewUrl)
            .build();
    }

    public ConfetiSong toConfetiSong() {
        return ConfetiSong.builder()
            .id(id)
            .trackName(trackName)
            .artworkUrl(artworkUrl)
            // TODO: 아티스트 이름 추가
            .build();
    }
}
