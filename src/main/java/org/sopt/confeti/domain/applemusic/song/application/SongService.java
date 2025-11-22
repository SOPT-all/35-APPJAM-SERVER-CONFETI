package org.sopt.confeti.domain.applemusic.song.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.applemusic.song.Song;
import org.sopt.confeti.domain.applemusic.song.application.dto.request.CreateSongDTO;
import org.sopt.confeti.domain.applemusic.song.infra.repository.SongRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SongService {

    private final SongRepository songRepository;

    public boolean isExistBySongId(String songId) {
        return songRepository.existsBySongId(songId);
    }

    @Transactional
    public String create(CreateSongDTO createSongDTO) {
        Song song = createSongDTO.toSong();
        return songRepository.save(song).getId();
    }

}
