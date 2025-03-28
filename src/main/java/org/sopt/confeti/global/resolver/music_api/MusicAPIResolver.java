package org.sopt.confeti.global.resolver.music_api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.resolver.music_api.artist.ArtistResolver;

@Resolver
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicAPIResolver {

    private final ArtistResolver artistResolver;

    /**
     * Apple Music API를 사용해 아티스트, 앨범, 음악 정보를 요청하는 엔트리 포인트
     * @param target
     * @param <T>
     */
    public <T> void load(final T target) {
        artistResolver.load(target);
    }
}
