package org.sopt.confeti.global.util.artistsearcher;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.annotation.Resolver;
import org.sopt.confeti.global.util.IntegrateFunction;
import org.springframework.aop.support.AopUtils;

@Resolver
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistResolver {

    private static final boolean ACCESS_ALLOW = true;

    private final Set<Class<?>> noCustomReferenceTypes = new LinkedHashSet<Class<?>>(
            Arrays.asList(
                Integer.class, Long.class, Float.class, Double.class,
                LocalDate.class, LocalDateTime.class, LocalTime.class,
                String.class
            )
    );

    private final ConcurrentHashMap<Class<?>, IntegrateFunction> collectByTypeMapper = new ConcurrentHashMap<Class<?>, IntegrateFunction>() {{
        put(ArrayList.class, args -> {
            if (!(args[0] instanceof List)) {
                // TODO:
                // 예외 처리 추가
                throw new RuntimeException();
            }

            collectByListType((List<?>) args[0]);
            return null;
        });
        put(List.class, args -> {
            if (!(args[0] instanceof List)) {
                // TODO:
                // 예외 처리 추가
                throw new RuntimeException();
            }

            collectByListType((List<?>) args[0]);
            return null;
        });
    }};

    private List<String> artistIds;
    private HashMap<String, ConfetiArtist> artistMapper;
    private HashMap<Object, Boolean> objectTrack;

    private final SpotifyAPIHandler spotifyAPIHandler;

    private void prologue() {
        artistIds = new ArrayList<>();
        artistMapper = new HashMap<>();
        objectTrack = new HashMap<>();
    }

    private void epilogue() {
        artistIds.clear();
        artistMapper.clear();
        objectTrack.clear();
    }

    // Spotify API를 사용해 아티스트를 로드하는 엔트리 포인트
    public void load(final Object target) {
        prologue();
        collect(target);

        List<ConfetiArtist> confetiArtists =  searchByArtistIds(artistIds);

        injection(confetiArtists);
        epilogue();
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

    // 리플렉션을 사용해 타겟 오브젝트를 재귀적으로 순회하며 아티스트 아이디를 수집하는 함수
    // TODO: 추후에 메소드 분리 리펙토링 예정
    private void collect(final Object target) {
        if (target !=null) {
            System.out.println(target);
        }
        // 이미 탐색한 객체인 경우
        if (target == null || objectTrack.containsKey(target)) {
            return;
        }

        // 오브젝트 트랙에 현재 탐색 대상 객체를 추가
        objectTrack.put(target, true);

        // 현재 오브젝트가 ConfetiArtist 타입인 경우
        if (isConfetiArtistClass(target)) {
            ConfetiArtist artist = (ConfetiArtist) target;

            artistIds.add(artist.getArtistId());
            artistMapper.put(artist.getArtistId(), artist);
            return;
        } else if (collectByTypeMapper.containsKey(target.getClass())) {
            // 현재 오브젝트가 정해진 처리 방법이 필요한 타입인 경우 (현재는 List)
            collectByTypeMapper.get(target.getClass()).apply(
                    target
            );
            return;
        }

        // 필드에 있는 객체 목록 확인
        Class<?> targetClass = target.getClass();
        Arrays.stream(targetClass.getDeclaredFields()).forEach((Field field) -> {
            if (isReferenceType(field) && isNoCustomReferenceType(field)) {
                if (isConfetiArtistClass(field)) {
                    ConfetiArtist artist = extractConfetiArtist(target, field);

                    artistIds.add(artist.getArtistId());
                    artistMapper.put(artist.getArtistId(), artist);
                    return;
                }

                setAccessibleIfPrivateOrProtected(field);

                try {
                    if (collectByTypeMapper.containsKey(field.getType())) {
                        collectByTypeMapper.get(field.getType()).apply(
                                field.get(target)
                        );

                        return;
                    }

                    collect(field.get(target));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void collectByListType(final List<?> objects) {
        objects.forEach(this::collect);
    }

    private void setAccessibleIfPrivateOrProtected(final Field field) {
        if (
                Modifier.isPrivate(field.getModifiers()) || Modifier.isProtected(field.getModifiers())
        ) {
            field.setAccessible(ACCESS_ALLOW);
        }
    }

    private boolean isNoCustomReferenceType(final Field field) {
        return !noCustomReferenceTypes.contains(field.getType());
    }

    private boolean isReferenceType(final Field field) {
        return !field.getType().isPrimitive();
    }

    private boolean isConfetiArtistClass(final Field field) {
        return field.getType().isAssignableFrom(ConfetiArtist.class);
    }

    private boolean isConfetiArtistClass(final Object object) {
        return object.getClass().isAssignableFrom(ConfetiArtist.class);
    }

    private ConfetiArtist extractConfetiArtist(final Object target, final Field field)
            throws RuntimeException {
        try {
            setAccessibleIfPrivateOrProtected(field);
            return (ConfetiArtist) field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }

    }
}
