package org.sopt.confeti.domain.setlist.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistMusicEditDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateMusicOrderDTO;
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

    private static final int SWAP_REQUEST_SIZE = 2;

    @Transactional
    public void startEdit(Long userId, Long setlistId) {
        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusic> musics = setlistMusicRepository.findBySetlist(setlist);

        List<SetlistMusicEditDTO> musicDtos = musics.stream()
                .map(SetlistMusicEditDTO::from)
                .toList();

        String redisKey = generateRedisKey(userId, setlistId);

        Boolean existed = redisTemplate.hasKey(redisKey);
        if (Boolean.TRUE.equals(existed)) {
            redisTemplate.delete(redisKey);
        }

        redisTemplate.opsForValue().set(redisKey, musicDtos);
    }

    @Transactional
    public void updateMusicOrder(Long userId, Long setlistId, List<SetlistUpdateMusicOrderDTO> requests) {
        String key = generateRedisKey(userId, setlistId);
        List<SetlistMusicEditDTO> musics = getRedisMusicList(key);

        Map<String, SetlistMusicEditDTO> musicMap = toMusicMap(musics);

        if (requests.size() == SWAP_REQUEST_SIZE) {
            swapOrders(musicMap, requests);
        }

        List<SetlistMusicEditDTO> updated = new ArrayList<>(musicMap.values()).stream()
                .sorted(Comparator.comparing(SetlistMusicEditDTO::orders))
                .toList();

        redisTemplate.opsForValue().set(key, updated);
    }

    @Transactional
    public String deleteMusic(Long userId, Long setlistId, int orders) {
        String key = generateRedisKey(userId, setlistId);
        Object raw = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(raw)) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        List<SetlistMusicEditDTO> musics = objectMapper.convertValue(raw, new TypeReference<>() {});
        if(musics.isEmpty()) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        SetlistMusicEditDTO deleted = musics.stream()
                .filter(m -> m.orders() == orders)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        musics = musics.stream()
                .filter(m -> m.orders() != orders)
                .sorted(Comparator.comparing(SetlistMusicEditDTO::orders))
                .toList();

        List<SetlistMusicEditDTO> reordered = new ArrayList<>();
        for (int i = 0; i < musics.size(); i++) {
            SetlistMusicEditDTO m = musics.get(i);
            reordered.add(new SetlistMusicEditDTO(
                    m.setlistMusicId(), m.musicId(), m.artistName(), m.trackName(),
                    m.artworkUrl(), m.previewUrl(), i + 1
            ));
        }

        redisTemplate.opsForValue().set(key, reordered);
        return deleted.musicId();
    }

    @Transactional
    public void completeEdit(Long userId, Long setlistId) {
        String key = generateRedisKey(userId, setlistId);
        Object raw = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(raw)) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        List<SetlistMusicEditDTO> edited = objectMapper.convertValue(raw, new TypeReference<>() {});
        if (edited.isEmpty()) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusic> original = setlistMusicRepository.findBySetlist(setlist);

        Map<Long, SetlistMusicEditDTO> editedMap = edited.stream()
                .collect(Collectors.toMap(SetlistMusicEditDTO::setlistMusicId, dto -> dto));

        List<SetlistMusic> toUpdate = new ArrayList<>();
        List<SetlistMusic> toDelete = new ArrayList<>();

        for (SetlistMusic music : original) {
            SetlistMusicEditDTO dto = editedMap.get(music.getId());
            if (Objects.nonNull(dto)) {
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

    private List<SetlistMusicEditDTO> getRedisMusicList(String key) {
        Object raw = redisTemplate.opsForValue().get(key);
        if(Objects.isNull(raw)) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        List<SetlistMusicEditDTO> musics = objectMapper.convertValue(raw, new TypeReference<>() {});
        if (musics.isEmpty()) throw new NotFoundException(ErrorMessage.NOT_FOUND);

        return musics;
    }

    private Map<String, SetlistMusicEditDTO> toMusicMap(List<SetlistMusicEditDTO> musics) {
        return musics.stream()
                .collect(Collectors.toMap(SetlistMusicEditDTO::musicId, dto -> dto));
    }

    private void swapOrders(Map<String, SetlistMusicEditDTO> musicMap, List<SetlistUpdateMusicOrderDTO> requests) {
        SetlistMusicEditDTO a = musicMap.get(requests.get(0).musicId());
        SetlistMusicEditDTO b = musicMap.get(requests.get(1).musicId());

        if (Objects.nonNull(a) && Objects.nonNull(b)) {
            int tmpOrder = a.orders();
            a = new SetlistMusicEditDTO(a.setlistMusicId(), a.musicId(), a.artistName(), a.trackName(), a.artworkUrl(), a.previewUrl(), b.orders());
            b = new SetlistMusicEditDTO(b.setlistMusicId(), b.musicId(), b.artistName(), b.trackName(), b.artworkUrl(), b.previewUrl(), tmpOrder);

            musicMap.put(a.musicId(), a);
            musicMap.put(b.musicId(), b);
        }
    }
}
