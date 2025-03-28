package org.sopt.confeti.global.resolver.music_api.artist.strategy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.strategy.AbstractMusicAPIStrategy;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ArtistStrategy extends AbstractMusicAPIStrategy<ConfetiArtist> {
}
