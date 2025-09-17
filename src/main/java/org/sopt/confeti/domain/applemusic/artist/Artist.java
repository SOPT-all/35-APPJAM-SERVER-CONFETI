package org.sopt.confeti.domain.applemusic.artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "artists")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 3000)
    private String artworkUrl;

    private Integer artworkWidth;

    private Integer artworkHeight;

    public static Artist create(String id, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        return Artist.builder()
            .id(id)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkWidth(artworkWidth)
            .artworkHeight(artworkHeight)
            .build();
    }

    @Builder
    private Artist(String id, String name, String artworkUrl, Integer artworkWidth,
        Integer artworkHeight) {
        this.id = id;
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
