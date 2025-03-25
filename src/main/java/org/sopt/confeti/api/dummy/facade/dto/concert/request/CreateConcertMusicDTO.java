package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import org.sopt.confeti.api.dummy.dto.concert.CreateConcertMusicRequest;

public record CreateConcertMusicDTO(
        String musicId
) {
    public static CreateConcertMusicDTO from(CreateConcertMusicRequest request) {
        return new CreateConcertMusicDTO(
                request.getMusicId()
        );
    }
}
