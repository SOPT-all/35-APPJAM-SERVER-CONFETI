package org.sopt.confeti.domain.music.relatedartist.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.application.MusicAPIService;
import org.sopt.confeti.domain.music.application.dto.CacheResult;
import org.sopt.confeti.domain.music.application.dto.FetchResult;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.application.dto.PersistResult;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.domain.music.artist.application.dto.ArtistInfo;
import org.sopt.confeti.domain.music.relatedartist.application.dto.RelatedArtistInfo;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatedArtistMusicAPIService extends MusicAPIService<RelatedArtistInfo> {

    private static final int FIXED_FETCH_SIZE = 20;

    private final RedisHandler redisHandler;
    private final RelatedArtistService relatedArtistService;
    private final ArtistService artistService;
    private final MusicAPIHandler musicAPIHandler;

    @Override
    protected void cache(List<RelatedArtistInfo> targetList) {
        String artistId = targetList.getFirst().artist().id();
        redisHandler.set(RedisKey.MUSIC_RELATED_ARTISTS.createKeyInfo(artistId), targetList);
    }

    @Override
    protected void persist(List<RelatedArtistInfo> targetList) {
        if (targetList.isEmpty()) {
            return;
        }
        String artistId = targetList.getFirst().artist().id();

        List<ArtistInfo> relatedArtistInfos = targetList.stream()
            .map(RelatedArtistInfo::relatedArtist)
            .toList();

        Set<String> relatedArtistIds = relatedArtistInfos.stream()
            .map(ArtistInfo::id)
            .collect(Collectors.toSet());

        Tx.masterTx(() -> {
            artistService.createFromArtistInfos(relatedArtistInfos);
            relatedArtistService.createRelatedArtists(artistId, relatedArtistIds);
        });
    }

    @Override
    protected CacheResult<RelatedArtistInfo> getCached(MusicAPICondition musicAPICondition) {
        String artistId = extractArtistId(musicAPICondition);
        List<RelatedArtistInfo> cachedArtists = redisHandler.getList(
            RedisKey.MUSIC_RELATED_ARTISTS.createKeyInfo(artistId));

        Set<String> cachedIds = cachedArtists.isEmpty() ? Set.of() : Set.of(artistId);
        return new CacheResult<>(cachedArtists, cachedIds);
    }

    @Override
    protected PersistResult<RelatedArtistInfo> getPersisted(MusicAPICondition musicAPICondition) {
        String artistId = extractArtistId(musicAPICondition);
        List<RelatedArtistInfo> persistedArtists = relatedArtistService.getRelatedArtistInfos(
            artistId, FIXED_FETCH_SIZE);

        if (!persistedArtists.isEmpty()) {
            cache(persistedArtists);
        }

        Set<String> persistedIds = persistedArtists.isEmpty() ? Set.of() : Set.of(artistId);
        return new PersistResult<>(persistedArtists, persistedIds);
    }

    @Override
    protected FetchResult<RelatedArtistInfo> getFetched(MusicAPICondition musicAPICondition) {
        String artistId = extractArtistId(musicAPICondition);

        List<ConfetiArtist> fetchedArtists = musicAPIHandler.getRelatedArtists(artistId,
            FIXED_FETCH_SIZE);
        List<RelatedArtistInfo> results = RelatedArtistInfo.fromConfetiArtists(artistId,
            fetchedArtists);

        if (!results.isEmpty()) {
            persist(results);
            cache(results);
        }

        return new FetchResult<>(results);
    }

    private String extractArtistId(MusicAPICondition condition) {
        return condition.extractSingleId();
    }
}
