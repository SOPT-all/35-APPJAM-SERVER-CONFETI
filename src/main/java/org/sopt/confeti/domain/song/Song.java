package org.sopt.confeti.domain.song;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "songs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Song {

    @Id
    @Column(name = "song_id")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String artworkUrl;

    private Integer artworkWidth;

    private Integer artworkHeight;

    public static Song create(String id, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        return Song.builder()
            .id(id)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkHeight(artworkHeight)
            .artworkWidth(artworkWidth)
            .build();
    }

    @Builder
    private Song(String id, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        this.id = id;
        this.name = name;
        this.artworkUrl = artworkUrl;
        this.artworkWidth = artworkWidth;
        this.artworkHeight = artworkHeight;
    }
}
