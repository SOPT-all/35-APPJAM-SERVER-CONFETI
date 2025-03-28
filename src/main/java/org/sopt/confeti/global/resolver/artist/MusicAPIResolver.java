package org.sopt.confeti.global.resolver.artist;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.music.MusicAPIHandler;

@Resolver
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MusicAPIResolver {

    private final MusicAPIHandler musicAPIHandler;
    private final ArtistStrategyRegistry artistStrategyRegistry;

    // Spotify API를 사용해 아티스트를 로드하는 엔트리 포인트
    public <T> void load(final T target) {
        final HashMap<String, Queue<ConfetiArtist>> artistMapper = new HashMap<>();

        collect(artistMapper, target);

        List<ConfetiArtist> confetiArtists =  getArtistsByArtistIds(artistMapper.keySet());

        injection(artistMapper, confetiArtists);
    }

    private <T> void collect(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final T target
    ) {
        if (isListType(target)) {
            collectList(artistMapper, (List<?>) target);
            return;
        }

        collectSingle(artistMapper, target);
    }

    private <T> void collectList(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final List<T> targets
    ) {
        if (targets.isEmpty()) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        ArtistStrategy artistStrategy = artistStrategyRegistry.getArtistStrategyByClass(targets.getFirst().getClass());
        targets.forEach(target -> {
            artistStrategy.collect(artistMapper, target);
        });
    }

    private <T> void collectSingle(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final T target
    ) {
        if (target == null) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        ArtistStrategy artistStrategy = artistStrategyRegistry.getArtistStrategyByClass(target.getClass());
        artistStrategy.collect(artistMapper, target);
    }

    private <T> boolean isListType(final T target) {
        return target instanceof List<?>;
    }

    private void injection(
            final HashMap<String, Queue<ConfetiArtist>> artistMapper,
            final List<ConfetiArtist> confetiArtists
    ) {
        confetiArtists.forEach((confetiArtist -> {
            Queue<ConfetiArtist> mappedConfetiArtists = artistMapper.get(confetiArtist.getArtistId());

            while (!mappedConfetiArtists.isEmpty()) {
                ConfetiArtist mappedConfetiArtist = mappedConfetiArtists.poll();

                mappedConfetiArtist.setName(confetiArtist.getName());
                mappedConfetiArtist.setProfileUrl(confetiArtist.getProfileUrl());
            }
        }));
    }

    private List<ConfetiArtist> getArtistsByArtistIds(final Set<String> artistIds) {
        return musicAPIHandler.getArtistsByArtistIds(artistIds);
    }
}
