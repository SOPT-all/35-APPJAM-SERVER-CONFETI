package org.sopt.confeti.global.messagebroker.sqs.message.artist;

import org.sopt.confeti.domain.music.artist.application.dto.request.CreateArtistDTO;
import org.sopt.confeti.global.messagebroker.message.CreateMessage;

public record CreateArtistMessage(
    String artistId,
    String name,
    String artworkUrl
) implements CreateMessage {

    public CreateArtistDTO toCreateDTO() {
        return CreateArtistDTO.builder()
            .artistId(artistId)
            .name(name)
            .artworkUrl(artworkUrl)
            .build();
    }
}
