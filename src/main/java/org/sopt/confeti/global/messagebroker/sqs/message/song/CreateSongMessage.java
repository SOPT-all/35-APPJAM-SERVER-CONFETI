package org.sopt.confeti.global.messagebroker.sqs.message.song;

import org.sopt.confeti.domain.music.song.application.dto.request.CreateSongDTO;
import org.sopt.confeti.global.messagebroker.message.CreateMessage;

public record CreateSongMessage(
    String songId,
    String name,
    String artworkUrl
) implements CreateMessage {

    public CreateSongDTO toCreateDTO() {
        return CreateSongDTO.builder()
            .songId(songId)
            .name(name)
            .artworkUrl(artworkUrl)
            .build();
    }
}
