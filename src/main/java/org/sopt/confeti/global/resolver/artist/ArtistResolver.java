package org.sopt.confeti.global.resolver.artist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.SpotifyAPIHandler;

@Resolver
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistResolver {

    private final SpotifyAPIHandler spotifyAPIHandler;
    private final ArtistStrategyRegistry artistStrategyRegistry;

    // Spotify API를 사용해 아티스트를 로드하는 엔트리 포인트
    public void load(final Object target) {
        final HashMap<String, Queue<ConfetiArtist>> artistMapper = new HashMap<>();

        collect(artistMapper, target, artistStrategyRegistry.getArtistStrategyByClass(target.getClass()));

        List<ConfetiArtist> confetiArtists =  searchByArtistIds(artistMapper.keySet());

        injection(artistMapper, confetiArtists);
    }

    private void collect(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final Object target,
            final ArtistStrategy artistStrategy
    ) {
        if (target == null) {
            return;
        }

        // 주어진 타겟이 리스트일 경우
        if (isListType(target)) {
            List<Object> targets = (List<Object>) target;

            targets.forEach(loadTarget -> {
                // Mapper에 등록된 클래스 타입인 경우
                artistStrategy.collect(artistMapper, loadTarget);
            });

            return;
        }
        // Mapper에 등록된 클래스 타입인 경우
        artistStrategy.collect(artistMapper, target);
    }

    private boolean isListType(final Object target) {
        return target.getClass() == ArrayList.class;
    }

    private void injection(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final List<ConfetiArtist> confetiArtists
    ) {
        confetiArtists.forEach((confetiArtist -> {
            ConfetiArtist mappedConfetiArtist = artistMapper.get(confetiArtist.getArtistId()).poll();

            if (mappedConfetiArtist == null) {
                throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
            }

            mappedConfetiArtist.setName(confetiArtist.getName());
            mappedConfetiArtist.setProfileUrl(confetiArtist.getProfileUrl());
        }));
    }

    private List<ConfetiArtist> searchByArtistIds(final Set<String> artistIds) {
        return spotifyAPIHandler.findArtistsByArtistIdsEntry(artistIds);
    }
}
