package org.sopt.confeti.domain.music.song.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.music.song.Song;
import org.sopt.confeti.domain.music.song.application.dto.request.CreateSongDTO;
import org.sopt.confeti.domain.music.song.infra.repository.SongRepository;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    @Transactional(readOnly = true)
    public boolean isExistBySongId(String songId) {
        return songRepository.existsById(songId);
    }

    @Transactional(readOnly = true)
    public List<Song> getSongs(Set<String> songIds) {
        return songRepository.findAllById(songIds);
    }

    @Transactional
    public String create(CreateSongDTO createSongDTO) {
        Song song = createSongDTO.toSong();
        return songRepository.save(song).getId();
    }

    @Transactional
    public void create(List<ConfetiSong> confetiSongs) {
        Set<String> requestedIds = confetiSongs.stream()
            .map(ConfetiSong::getId)
            .collect(Collectors.toSet());

        Set<String> existingIds = songRepository.findAllById(requestedIds).stream()
            .map(Song::getId)
            .collect(Collectors.toSet());

        List<Song> newSongs = confetiSongs.stream()
            .filter(song -> !existingIds.contains(song.getId()))
            .map(ConfetiSong::toSong)
            .toList();

        songRepository.saveAll(newSongs);
    }
}
