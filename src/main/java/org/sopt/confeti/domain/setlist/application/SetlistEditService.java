package org.sopt.confeti.domain.setlist.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistMusicEditDto;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistMusicOrderUpdateRequest;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistMusicRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SetlistEditService {

    private final SetlistRepository setlistRepository;
    private final SetlistMusicRepository setlistMusicRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional
    public void startEdit(Long userId, Long setlistId) {
        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusic> musics = setlistMusicRepository.findBySetlist(setlist);
        List<SetlistMusicEditDto> musicDtos = musics.stream()
                .map(SetlistMusicEditDto::from)
                .toList();

        String redisKey = generateRedisKey(userId, setlistId);
        redisTemplate.opsForValue().set(redisKey, musicDtos);
    }

    @Transactional
    public void updateMusicOrder(Long userId, Long setlistId, List<SetlistMusicOrderUpdateRequest> requests) {
        String key = generateRedisKey(userId, setlistId);
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        List<SetlistMusicEditDto> musics = objectMapper.convertValue(raw, new TypeReference<>() {});
        if (musics.isEmpty()) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        Map<String, SetlistMusicEditDto> musicMap = musics.stream()
                .collect(Collectors.toMap(SetlistMusicEditDto::trackId, dto -> dto));

        if (requests.size() == 2) {
            SetlistMusicEditDto a = musicMap.get(requests.get(0).trackId());
            SetlistMusicEditDto b = musicMap.get(requests.get(1).trackId());

            if (a != null && b != null) {
                int tmpOrder = a.orders();
                a = new SetlistMusicEditDto(a.musicId(), a.trackId(), a.artistName(), a.trackName(), a.artworkUrl(), a.previewUrl(), b.orders());
                b = new SetlistMusicEditDto(b.musicId(), b.trackId(), b.artistName(), b.trackName(), b.artworkUrl(), b.previewUrl(), tmpOrder);

                musicMap.put(a.trackId(), a);
                musicMap.put(b.trackId(), b);
            }
        }

        List<SetlistMusicEditDto> updated = new ArrayList<>(musicMap.values()).stream()
                .sorted(Comparator.comparing(SetlistMusicEditDto::orders))
                .toList();

        redisTemplate.opsForValue().set(key, updated);
    }

    @Transactional
    public String deleteMusic(Long userId, Long setlistId, int orders) {
        String key = generateRedisKey(userId, setlistId);
        Object raw = redisTemplate.opsForValue().get(key);
        if(raw == null) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        List<SetlistMusicEditDto> musics = objectMapper.convertValue(raw, new TypeReference<>() {});
        if(musics.isEmpty()) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        SetlistMusicEditDto deleted = musics.stream()
                .filter(m -> m.orders() == orders)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        musics = musics.stream()
                .filter(m -> m.orders() != orders)
                .sorted(Comparator.comparing(SetlistMusicEditDto::orders))
                .toList();

        List<SetlistMusicEditDto> reordered = new ArrayList<>();
        for (int i = 0; i < musics.size(); i++) {
            SetlistMusicEditDto m = musics.get(i);
            reordered.add(new SetlistMusicEditDto(
                    m.musicId(), m.trackId(), m.artistName(), m.trackName(),
                    m.artworkUrl(), m.previewUrl(), i + 1
            ));
        }

        redisTemplate.opsForValue().set(key, reordered);
        return deleted.trackId();
    }

    @Transactional
    public void completeEdit(Long userId, Long setlistId) {
        String key = generateRedisKey(userId, setlistId);
        Object raw = redisTemplate.opsForValue().get(key);
        if (raw == null) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        List<SetlistMusicEditDto> edited = objectMapper.convertValue(raw, new TypeReference<>() {});
        if (edited.isEmpty()) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusic> original = setlistMusicRepository.findBySetlist(setlist);

        Map<Long, SetlistMusicEditDto> editedMap = edited.stream()
                .collect(Collectors.toMap(SetlistMusicEditDto::musicId, dto -> dto));

        List<SetlistMusic> toUpdate = new ArrayList<>();
        List<SetlistMusic> toDelete = new ArrayList<>();

        for (SetlistMusic music : original) {
            SetlistMusicEditDto dto = editedMap.get(music.getId());
            if (dto != null) {
                music.changeOrder(dto.orders());
                toUpdate.add(music);
            } else {
                toDelete.add(music);
            }
        }

        if (!toUpdate.isEmpty()) {
            setlistMusicRepository.saveAll(toUpdate);
        }
        if (!toDelete.isEmpty()) {
            setlistMusicRepository.deleteAll(toDelete);
        }

        redisTemplate.delete(key);
    }

    @Transactional
    public void cancelEdit(Long userId, Long setlistId) {
        String key = generateRedisKey(userId, setlistId);
        Boolean existed = redisTemplate.hasKey(key);
        System.out.println(existed);
        if (Boolean.FALSE.equals(existed)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
        redisTemplate.delete(key);
    }

    private String generateRedisKey(Long userId, Long setlistId) {
        return "edit:setlist:" + userId + ":" + setlistId;
    }
}
