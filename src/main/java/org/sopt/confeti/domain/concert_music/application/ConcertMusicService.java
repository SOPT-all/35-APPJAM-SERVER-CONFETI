package org.sopt.confeti.domain.concert_music.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert_music.infra.repository.ConcertMusicRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertMusicService {

    private final ConcertMusicRepository concertMusicRepository;

}
