package org.sopt.confeti.domain.top_artist;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.artist.Artist;

@Entity
@Table(name = "top_artists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TopArtist {

    @Id
    @Column(name = "rank")
    private Long rank;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

}
