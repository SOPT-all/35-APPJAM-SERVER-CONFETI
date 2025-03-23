package org.sopt.confeti.domain.festival_music.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.festival_music.infra.repository.FestivalMusicRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalMusicService {

    private final FestivalMusicRepository festivalMusicRepository;

}
