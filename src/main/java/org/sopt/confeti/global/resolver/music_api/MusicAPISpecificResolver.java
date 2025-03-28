package org.sopt.confeti.global.resolver.music_api;

import reactor.core.publisher.Mono;

public interface MusicAPISpecificResolver {

    <T> void load(final T target);
}
