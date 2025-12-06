package org.sopt.confeti.domain.setlist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddSongDTO;

@Entity
@Table(name = "setlist_songs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SetlistSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setlist_id", nullable = false)
    private Setlist setlist;

    @Column(nullable = false)
    private String songId;

    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column(name = "track_name", nullable = false)
    private String trackName;

    @Column(name = "artwork_url")
    private String artworkUrl;

    @Column(name = "preview_url")
    private String previewUrl;

    @Column(name = "orders", nullable = false)
    private int orders;

    @Builder
    public SetlistSong(String songId, String artistName, String trackName, String artworkUrl,
        String previewUrl, int orders) {
        this.songId = songId;
        this.artistName = artistName;
        this.trackName = trackName;
        this.artworkUrl = artworkUrl;
        this.previewUrl = previewUrl;
        this.orders = orders;
    }

    public static SetlistSong of(SetlistAddSongDTO dto, int order) {
        return SetlistSong.builder()
            .songId(dto.songId())
            .artistName(dto.artistName())
            .trackName(dto.trackName())
            .artworkUrl(dto.artworkUrl())
            .previewUrl(dto.previewUrl())
            .orders(order)
            .build();
    }

    public void setSetlist(Setlist setlist) {
        this.setlist = setlist;
    }

    public void changeOrder(int newOrder) {
        this.orders = newOrder;
    }
}
