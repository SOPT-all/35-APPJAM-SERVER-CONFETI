package org.sopt.confeti.global.resolver.music_api;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.resolver.music_api.artist.ArtistResolver;
import reactor.core.publisher.Mono;

@Resolver
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicAPIResolver {

    private final ArtistResolver artistResolver;

    /**
     * Apple Music API를 사용해 아티스트, 앨범, 음악 정보를 요청하는 엔트리 포인트
     * @param target target 정보를 로드할 대상 객체
     * @param <T> 대상 객체 타입
     * @return 비동기 작업 완료 상태의 Mono
     */
    public <T> Mono<Void> load(final T target) {
        return Mono.when(
                artistResolver.load(target)
        );
    }
}
