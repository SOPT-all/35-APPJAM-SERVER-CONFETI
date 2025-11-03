package org.sopt.confeti.domain.setlist.application;

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
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SetlistEditService {

    private final SetlistRepository setlistRepository;
    private final SetlistMusicRepository setlistMusicRepository;
    private final RedisHandler redisHandler;

    private static final int SWAP_REQUEST_SIZE = 2;

    @Transactional
    public void startEdit(Long userId, Long setlistId) {
        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusic> musics = setlistMusicRepository.findBySetlist(setlist);

        List<SetlistMusicEditDTO> musicDtos = musics.stream()
                .map(SetlistMusicEditDTO::from)
                .toList();

        boolean existed = redisHandler.hasKey(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
        if (existed) {
            redisHandler.delete(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
        }

        cacheMusics(userId, setlistId, musicDtos);
    }

    @Transactional
    public void updateMusicOrder(Long userId, Long setlistId, List<SetlistUpdateMusicOrderDTO> requests) {
        List<SetlistMusicEditDTO> musics = getCachedMusics(userId, setlistId);

        Map<String, SetlistMusicEditDTO> musicMap = toMusicMap(musics);

        if (requests.size() == SWAP_REQUEST_SIZE) {
            swapOrders(musicMap, requests);
        }

        List<SetlistMusicEditDTO> updated = new ArrayList<>(musicMap.values()).stream()
                .sorted(Comparator.comparing(SetlistMusicEditDTO::orders))
                .toList();

        cacheMusics(userId, setlistId, updated);
    }

    @Transactional
    public String deleteMusic(Long userId, Long setlistId, int orders) {
        List<SetlistMusicEditDTO> musics = getCachedMusics(userId, setlistId);

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

        cacheMusics(userId, setlistId, reordered);
        return deleted.musicId();
    }

    @Transactional
    public void completeEdit(Long userId, Long setlistId) {
        List<SetlistMusicEditDTO> edited = getCachedMusics(userId, setlistId);

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

        redisHandler.delete(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
    }

    @Transactional
    public void cancelEdit(Long userId, Long setlistId) {
        boolean existed = redisHandler.hasKey(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));

        if (!existed) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        redisHandler.delete(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
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

    private List<SetlistMusicEditDTO> getCachedMusics(Long userId, Long setlistId) {
        List<SetlistMusicEditDTO> musics = redisHandler.getList(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));

        if (musics.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        return musics;
    }

    private void cacheMusics(Long userId, Long setlistId, List<SetlistMusicEditDTO> musics) {
        redisHandler.set(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId), musics);
    }
}
