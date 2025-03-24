package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalMusicRequest;

public record CreateFestivalMusicDTO(
        String musicId
) {
    public static CreateFestivalMusicDTO from(CreateFestivalMusicRequest request) {
        return new CreateFestivalMusicDTO(
                request.getMusicId()
        );
    }
}
