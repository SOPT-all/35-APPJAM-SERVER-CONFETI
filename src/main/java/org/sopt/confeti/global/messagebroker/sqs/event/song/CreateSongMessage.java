package org.sopt.confeti.global.messagebroker.sqs.event.song;


import org.sopt.confeti.domain.applemusic.song.application.dto.request.CreateSongDTO;
import org.sopt.confeti.global.messagebroker.message.CreateMessage;

public record CreateSongMessage(
    String songId,
    String name,
    String artworkUrl,
    Integer artworkWidth,
    Integer artworkHeight
) implements CreateMessage {

    public CreateSongDTO toCreateDTO() {
        return CreateSongDTO.builder()
            .songId(songId)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkWidth(artworkWidth)
            .artworkHeight(artworkHeight)
            .build();
    }

}
