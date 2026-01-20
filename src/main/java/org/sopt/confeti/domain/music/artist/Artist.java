package org.sopt.confeti.domain.music.artist;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.music.artistsong.ArtistSong;
import org.sopt.confeti.domain.music.relatedartist.RelatedArtist;
import org.sopt.confeti.domain.music.song.Song;
import org.sopt.confeti.global.common.constant.ArtistConstant;
import org.sopt.confeti.global.common.constant.Default;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.util.InvalidUrlException;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Entity
@Table(name = "artists")
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(length = 100)
    private String name;

    /*
     * Apple Music에서 width와 height를 커스텀할 수 있도록 아래의 형식으로 온다.
     * ex) https://~~{w}-{y}.png
     * 필요에 맞춰 width, height 를 넣어야한다.
     */
    @Column(length = 3000)
    private String artworkUrl;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistSong> songs = new ArrayList<>();

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RelatedArtist> relatedArtists = new ArrayList<>();

    @Builder
    private Artist(String artistId, String name, String artworkUrl) {
        this.id = artistId;
        this.name = name;
        this.artworkUrl = artworkUrl;
    }

    public static Artist create(String artistId) {
        return Artist.builder()
            .artistId(artistId)
            .build();
    }

    public static Artist create(String artistId, String name, String artworkUrl) {
        return Artist.builder()
            .artistId(artistId)
            .name(name)
            .artworkUrl(artworkUrl)
            .build();
    }

    public ConfetiArtist toConfetiArtist() {
        return ConfetiArtist.of(id, name, getProfileUrl());
    }

    public void addSong(Song song) {
        this.songs.add(
            ArtistSong.create(this, song)
        );
    }

    public void addRelatedArtist(Artist relatedArtist) {
        this.relatedArtists.add(RelatedArtist.create(this, relatedArtist));
    }

    public String getProfileUrl() {
        try {
            return UriComponentsBuilder.fromUriString(artworkUrl)
                .buildAndExpand(ArtistConstant.PROFILE_IMG_SIZE)
                .toUriString();
        } catch (InvalidUrlException e) {
            log.error("Artist.getProfileUrl : Invalid url. url : {}", artworkUrl);
            return Default.CONFETI_LOGO_IMG_URL;
        }
    }
}
