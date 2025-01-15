package org.sopt.confeti.global.util.artistsearcher;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {

    @Column(length = 100)
    private String artistId;

    @Transient
    private String name;

    @Transient
    private String profileUrl;

    public Artist(String artistId) {
        this.artistId = artistId;
    }
}
