package org.sopt.confeti.global.util.artistsearcher;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.util.IntegrateFunction;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistResolver {

    private static final String GET_ID_FUNC_NAME = "getId";
    private static final boolean ACCESS_ALLOW = true;

    private final Set<Class<?>> noCustomReferenceTypes = new LinkedHashSet<Class<?>>(
            Arrays.asList(
                Integer.class, Long.class, Float.class, Double.class,
                LocalDate.class, LocalDateTime.class, LocalTime.class,
                String.class
            )
    );

    private final ConcurrentHashMap<Class<?>, IntegrateFunction> collectByTypeMapper = new ConcurrentHashMap<Class<?>, IntegrateFunction>() {{
        put(List.class, args -> {
            if (!(args[0] instanceof List) || !(args[1] instanceof List) || !(args[2] instanceof HashMap)) {
                // TODO:
                // 예외 처리 추가
                throw new RuntimeException();
            }

            collectByListType((List<?>) args[0], (List<String>) args[1], (HashMap<String, ConfetiArtist>) args[2]);
            return null;
        });
    }};

    private SpotifyAPIHandler spotifyAPIHandler;

    public ArtistResolver(SpotifyAPIHandler spotifyAPIHandler) {
        this.spotifyAPIHandler = spotifyAPIHandler;
    }

    // Spotify API를 사용해 아티스트를 로드하는 엔트리 포인트
    public void load(final Object target) {
        List<String> artistIds = new ArrayList<>();
        HashMap<String, ConfetiArtist> artistMapper = new HashMap<>();
        collect(target, artistIds, artistMapper);

        List<ConfetiArtist> confetiArtists =  searchByArtistIds(artistIds);

        injection(artistMapper, confetiArtists);
    }

    private void injection(final HashMap<String, ConfetiArtist> artistMapper, final List<ConfetiArtist> confetiArtists) {
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
    private void collect(final Object target, final List<String> artistIds, final HashMap<String, ConfetiArtist> artistMapper) {
        Class<?> targetClass = target.getClass();
        Arrays.stream(targetClass.getDeclaredFields()).forEach((Field field) -> {
            if (isReferenceType(field) && isNoCustomReferenceType(field)) {
                if (isArtistClass(field)) {
                    String artistId = extractArtistId(target);
                    artistIds.add(artistId);
                    artistMapper.put(artistId, (ConfetiArtist) target);
                    return;
                }

                try {
                    collectByType(target, field, artistIds, artistMapper);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void collectByType(final Object target, final Field field, final List<String> artistIds, final HashMap<String, ConfetiArtist> artistMapper) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        setAccessibleIfPrivate(field);

        try {
            collectByTypeMapper.get(fieldType).apply(
                    field.get(target),
                    artistIds,
                    artistMapper
            );
        } catch (NullPointerException e) {
            collect(
                    field.get(target),
                    artistIds,
                    artistMapper
            );
        }
    }

    private void collectByListType(final List<?> objects, final List<String> artistIds, final HashMap<String, ConfetiArtist> artistMapper) {
        objects.forEach((obj) -> collect(obj, artistIds, artistMapper));
    }

    private void setAccessibleIfPrivate(final Field field) {
        if (Modifier.isPrivate(field.getModifiers())) {
            field.setAccessible(ACCESS_ALLOW);
        }
    }

    private boolean isNoCustomReferenceType(final Field field) {
        return !noCustomReferenceTypes.contains(field.getClass());
    }

    private boolean isReferenceType(final Field field) {
        return !field.getClass().isPrimitive();
    }

    private boolean isArtistClass(final Field field) {
        return field.getType().isAssignableFrom(ConfetiArtist.class);
    }



    private String extractArtistId(final Object target)
            throws RuntimeException {
        try {
            return (String) target.getClass().getMethod(GET_ID_FUNC_NAME).invoke(target);
        } catch (NoSuchMethodException  | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException();
        }

    }
}
