package org.sopt.confeti.domain.setlist;

import jakarta.persistence.*;
import lombok.*;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddMusicDTO;

@Entity
@Table(name = "setlist_musics")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SetlistMusic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setlist_id", nullable = false)
    private Setlist setlist;

    @Column(nullable = false)
    private String trackId;

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

    public static SetlistMusic of(SetlistAddMusicDTO dto, int order) {
        return SetlistMusic.builder()
                .trackId(dto.trackId())
                .artistName(dto.artistName())
                .trackName(dto.trackName())
                .artworkUrl(dto.artworkUrl())
                .previewUrl(dto.previewUrl())
                .orders(order)
                .build();
    }

    @Builder
    public SetlistMusic(String trackId, String artistName, String trackName, String artworkUrl, String previewUrl, int orders) {
        this.trackId = trackId;
        this.artistName = artistName;
        this.trackName = trackName;
        this.artworkUrl = artworkUrl;
        this.previewUrl = previewUrl;
        this.orders = orders;
    }

    public void setSetlist(Setlist setlist) {
        this.setlist = setlist;
    }

    public void changeOrder(int newOrder) {
        this.orders = newOrder;
    }
}