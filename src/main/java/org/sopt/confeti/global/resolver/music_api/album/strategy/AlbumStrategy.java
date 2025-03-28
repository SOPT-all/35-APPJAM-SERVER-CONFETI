package org.sopt.confeti.global.resolver.music_api.album.strategy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.resolver.music_api.strategy.AbstractMusicAPIStrategy;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AlbumStrategy extends AbstractMusicAPIStrategy<ConfetiAlbum> {
}
