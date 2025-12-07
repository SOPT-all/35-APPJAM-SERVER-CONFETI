package org.sopt.confeti.domain.setlist.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistSongEditDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateSongOrderDTO;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistSong;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistSongRepository;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SetlistEditService {

    private static final int SWAP_REQUEST_SIZE = 2;
    private final SetlistRepository setlistRepository;
    private final SetlistSongRepository setlistSongRepository;
    private final RedisHandler redisHandler;

    @Transactional
    public void startEdit(Long userId, Long setlistId) {
        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistSong> songs = setlistSongRepository.findBySetlist(setlist);

        List<SetlistSongEditDTO> songDtos = songs.stream()
            .map(SetlistSongEditDTO::from)
            .toList();

        boolean existed = redisHandler.hasKey(
            RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
        if (existed) {
            redisHandler.delete(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
        }

        cacheSongs(userId, setlistId, songDtos);
    }

    @Transactional
    public void updateSongOrder(Long userId, Long setlistId,
        List<SetlistUpdateSongOrderDTO> requests) {
        List<SetlistSongEditDTO> songs = getCachedSongs(userId, setlistId);

        Map<String, SetlistSongEditDTO> songMap = toSongMap(songs);

        if (requests.size() == SWAP_REQUEST_SIZE) {
            swapOrders(songMap, requests);
        }

        List<SetlistSongEditDTO> updated = new ArrayList<>(songMap.values()).stream()
            .sorted(Comparator.comparing(SetlistSongEditDTO::orders))
            .toList();

        cacheSongs(userId, setlistId, updated);
    }

    @Transactional
    public String deleteSong(Long userId, Long setlistId, int orders) {
        List<SetlistSongEditDTO> songs = getCachedSongs(userId, setlistId);

        SetlistSongEditDTO deleted = songs.stream()
            .filter(m -> m.orders() == orders)
            .findFirst()
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        songs = songs.stream()
            .filter(m -> m.orders() != orders)
            .sorted(Comparator.comparing(SetlistSongEditDTO::orders))
            .toList();

        List<SetlistSongEditDTO> reordered = new ArrayList<>();
        for (int i = 0; i < songs.size(); i++) {
            SetlistSongEditDTO m = songs.get(i);
            reordered.add(new SetlistSongEditDTO(
                m.setlistSongId(), m.songId(), m.artistName(), m.trackName(),
                m.artworkUrl(), m.previewUrl(), i + 1
            ));
        }

        cacheSongs(userId, setlistId, reordered);
        return deleted.songId();
    }

    @Transactional
    public void completeEdit(Long userId, Long setlistId) {
        List<SetlistSongEditDTO> edited = getCachedSongs(userId, setlistId);

        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistSong> original = setlistSongRepository.findBySetlist(setlist);

        Map<Long, SetlistSongEditDTO> editedMap = edited.stream()
            .collect(Collectors.toMap(SetlistSongEditDTO::setlistSongId, dto -> dto));

        List<SetlistSong> toUpdate = new ArrayList<>();
        List<SetlistSong> toDelete = new ArrayList<>();

        for (SetlistSong song : original) {
            SetlistSongEditDTO dto = editedMap.get(song.getId());
            if (Objects.nonNull(dto)) {
                song.changeOrder(dto.orders());
                toUpdate.add(song);
            } else {
                toDelete.add(song);
            }
        }

        if (!toUpdate.isEmpty()) {
            setlistSongRepository.saveAll(toUpdate);
        }
        if (!toDelete.isEmpty()) {
            setlistSongRepository.deleteAll(toDelete);
        }

        redisHandler.delete(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
    }

    @Transactional
    public void cancelEdit(Long userId, Long setlistId) {
        boolean existed = redisHandler.hasKey(
            RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));

        if (!existed) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        redisHandler.delete(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));
    }

    private Map<String, SetlistSongEditDTO> toSongMap(List<SetlistSongEditDTO> songs) {
        return songs.stream()
            .collect(Collectors.toMap(SetlistSongEditDTO::songId, dto -> dto));
    }

    private void swapOrders(Map<String, SetlistSongEditDTO> songMap,
        List<SetlistUpdateSongOrderDTO> requests) {
        SetlistSongEditDTO a = songMap.get(requests.get(0).songId());
        SetlistSongEditDTO b = songMap.get(requests.get(1).songId());

        if (Objects.nonNull(a) && Objects.nonNull(b)) {
            int tmpOrder = a.orders();
            a = new SetlistSongEditDTO(a.setlistSongId(), a.songId(), a.artistName(),
                a.trackName(), a.artworkUrl(), a.previewUrl(), b.orders());
            b = new SetlistSongEditDTO(b.setlistSongId(), b.songId(), b.artistName(),
                b.trackName(), b.artworkUrl(), b.previewUrl(), tmpOrder);

            songMap.put(a.songId(), a);
            songMap.put(b.songId(), b);
        }
    }

    private List<SetlistSongEditDTO> getCachedSongs(Long userId, Long setlistId) {
        List<SetlistSongEditDTO> songs = redisHandler.getList(
            RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId));

        if (songs.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        return songs;
    }

    private void cacheSongs(Long userId, Long setlistId, List<SetlistSongEditDTO> songs) {
        redisHandler.set(RedisKey.SETLIST_EDIT.createKeyInfo(userId, setlistId), songs);
    }
}
