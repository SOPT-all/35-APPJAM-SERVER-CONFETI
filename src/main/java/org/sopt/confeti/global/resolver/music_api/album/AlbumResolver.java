package org.sopt.confeti.global.resolver.music_api.album;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Resolver;
import org.sopt.confeti.global.resolver.music_api.MusicAPISpecificResolver;
import org.sopt.confeti.global.resolver.music_api.album.strategy.AlbumStrategy;
import org.sopt.confeti.global.resolver.music_api.album.strategy.AlbumStrategyRegistry;
import org.sopt.confeti.global.resolver.music_api.album.vo.ConfetiAlbum;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Resolver
@RequiredArgsConstructor
public class AlbumResolver implements MusicAPISpecificResolver {

    private final MusicAPIHandler musicAPIHandler;
    private final AlbumStrategyRegistry albumStrategyRegistry;

    @Override
    @Transactional
    public <T> void load(T target) {
        if (isEmpty(target)) {
            return;
        }

        final AlbumStrategy strategy = getStrategy(target);
        if (notSupports(strategy)) {
            return;
        }

        final HashMap<String, Queue<ConfetiAlbum>> albumMapper = new HashMap<>();
        collect(strategy, albumMapper, target);
        injection(
                albumMapper, getAlbumsByAlbumIds(albumMapper.keySet())
        );
    }

    private <T> boolean isEmpty(T target) {
        return target == null;
    }

    private <T> AlbumStrategy getStrategy(T target) {
        return albumStrategyRegistry.getAlbumStrategyByClass(target.getClass());
    }

    private boolean notSupports(AlbumStrategy strategy) {
        return strategy == null;
    }

    private <T> void collect(
            final AlbumStrategy strategy,
            final HashMap<String, Queue<ConfetiAlbum>> albumMapper,
            final T target
    ) {
        if (isListType(target)) {
            collectList(strategy, albumMapper, (List<?>) target);
            return;
        }

        collectSingle(strategy, albumMapper, target);
    }

    private <T> void collectList(
            final AlbumStrategy strategy,
            final HashMap<String, Queue<ConfetiAlbum>> albumMapper,
            final List<T> targets
    ) {
        targets.forEach(target -> {
            strategy.collect(albumMapper, target);
        });
    }

    private <T> void collectSingle(
            final AlbumStrategy strategy,
            final HashMap<String, Queue<ConfetiAlbum>> albumMapper,
            final T target
    ) {
        strategy.collect(albumMapper, target);
    }

    private <T> boolean isListType(final T target) {
        return target instanceof List<?>;
    }

    private void injection(
            final HashMap<String, Queue<ConfetiAlbum>> albumMapper,
            final List<ConfetiAlbum> confetiAlbums
    ) {
        confetiAlbums.forEach((confetiAlbum -> {
            Queue<ConfetiAlbum> mappedConfetiAlbums = albumMapper.get(confetiAlbum.getId());

            while (!mappedConfetiAlbums.isEmpty()) {
                ConfetiAlbum mappedConfetiAlbum = mappedConfetiAlbums.poll();

                mappedConfetiAlbum.setName(confetiAlbum.getName());
                mappedConfetiAlbum.setReleaseAt(confetiAlbum.getReleaseAt());
            }
        }));
    }

    private List<ConfetiAlbum> getAlbumsByAlbumIds(final Set<String> albumIds) {
        return musicAPIHandler.getAlbumsByAlbumIds(albumIds);
    }
}
