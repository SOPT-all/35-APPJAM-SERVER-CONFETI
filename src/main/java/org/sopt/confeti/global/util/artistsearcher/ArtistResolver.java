package org.sopt.confeti.global.util.artistsearcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Resolver;
import org.sopt.confeti.domain.artistfavorite.ArtistFavorite;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festivaldate.FestivalDate;
import org.sopt.confeti.global.util.IntegrateFunction;

@Resolver
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistResolver {

    // 기존에 조회할 클래스를 매핑
    private final ConcurrentHashMap<Class<?>, IntegrateFunction> collectByTypeMapper = new ConcurrentHashMap<Class<?>, IntegrateFunction>() {{
        put(ArtistFavorite.class, args -> {
            collectArtistFavorite(args[0]);
            return null;
        });
        put(Concert.class, args -> {
            collectConcert(args[0]);
            return null;
        });
        put(Festival.class, args -> {
            collectFestival(args[0]);
            return null;
        });
        put(FestivalDate.class, args -> {
            collectFestivalDate(args[0]);
            return null;
        });
    }};

    private List<String> artistIds;
    private HashMap<String, ConfetiArtist> artistMapper;

    private final SpotifyAPIHandler spotifyAPIHandler;

    private void prologue() {
        artistIds = new ArrayList<>();
        artistMapper = new HashMap<>();
    }

    private void epilogue() {
        artistIds.clear();
        artistMapper.clear();
    }

    // Spotify API를 사용해 아티스트를 로드하는 엔트리 포인트
    public void load(final Object target) {
        prologue();
        collect(target);

        List<ConfetiArtist> confetiArtists =  searchByArtistIds(artistIds);

        injection(confetiArtists);
        epilogue();
    }

    private void collect(final Object target) {
        if (target == null) {
            return;
        }

        // 주어진 타겟이 리스트일 경우
        if (isListType(target)) {
            List<Object> objects = (List<Object>) target;

            objects.forEach(object -> {
                // Mapper에 등록된 클래스 타입인 경우
                if (collectByTypeMapper.containsKey(object.getClass())) {
                    collectByTypeMapper.get(object.getClass())
                            .apply(object);
                }
            });

            return;
        }
        // Mapper에 등록된 클래스 타입인 경우
        if (collectByTypeMapper.containsKey(target.getClass())) {
            collectByTypeMapper.get(target.getClass()).apply(target);
        }
    }

    private boolean isListType(final Object target) {
        return target.getClass() == ArrayList.class;
    }

    private void collectArtistFavorite(final Object target) {
        ArtistFavorite artistFavorite = (ArtistFavorite) target;
        ConfetiArtist confetiArtist = artistFavorite.getArtist();
        artistIds.add(confetiArtist.getArtistId());
        artistMapper.put(confetiArtist.getArtistId(), artistFavorite.getArtist());
    }

    private void collectConcert(final Object target) {
        Concert concert = (Concert) target;
        concert.getArtists().forEach(artist -> {
                    artistIds.add(artist.getArtist().getArtistId());
                    artistMapper.put(
                            artist.getArtist().getArtistId(),
                            artist.getArtist()
                    );
                });
    }

    private void collectFestival(final Object target) {
        Festival festival = (Festival) target;
        festival.getDates().stream()
                .flatMap(date -> date.getStages().stream())
                .flatMap(stage -> stage.getTimes().stream())
                .flatMap(time -> time.getArtists().stream())
                .forEach(artist -> {
                    artistIds.add(artist.getArtist().getArtistId());
                    artistMapper.put(artist.getArtist().getArtistId(), artist.getArtist());
                });
    }

    private void collectFestivalDate(final Object target) {
        FestivalDate festivalDate = (FestivalDate) target;
        festivalDate.getStages().stream()
                .flatMap(stage -> stage.getTimes().stream())
                .flatMap(time -> time.getArtists().stream())
                .forEach(artist -> {
                    artistIds.add(artist.getArtist().getArtistId());
                    artistMapper.put(artist.getArtist().getArtistId(), artist.getArtist());
                });
    }

    private void injection(final List<ConfetiArtist> confetiArtists) {
        confetiArtists.forEach((confetiArtist -> {
            ConfetiArtist mappedConfetiArtist = artistMapper.get(confetiArtist.getArtistId());
            mappedConfetiArtist.setName(confetiArtist.getName());
            mappedConfetiArtist.setProfileUrl(confetiArtist.getProfileUrl());
        }));
    }

    private List<ConfetiArtist> searchByArtistIds(final List<String> artistIds) {
        return spotifyAPIHandler.findArtistsByArtistIds(artistIds);
    }
}
