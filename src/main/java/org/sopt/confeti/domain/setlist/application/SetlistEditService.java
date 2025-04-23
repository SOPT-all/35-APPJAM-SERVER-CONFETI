package org.sopt.confeti.domain.setlist.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class SetlistEditService {

    private final SetlistRepository setlistRepository;
    private final SetlistMusicRepository setlistMusicRepository;
    private final RedisTemplate<String, List<SetlistMusicEditDto>> redisTemplate;
    private final ObjectMapper objectMapper;

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

    private String generateRedisKey(Long userId, Long setlistId) {
        return "edit:setlist:" + userId + ":" + setlistId;
    }
}
