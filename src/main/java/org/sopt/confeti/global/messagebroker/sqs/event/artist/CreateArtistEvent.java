package org.sopt.confeti.global.messagebroker.sqs.event.artist;


import org.sopt.confeti.domain.applemusic.artist.application.dto.request.CreateArtistDTO;
import org.sopt.confeti.global.messagebroker.message.CreateEvent;

public record CreateArtistEvent(
    String artistId,
    String name,
    String artworkUrl,
    Integer artworkWidth,
    Integer artworkHeight
) implements CreateEvent {

    public CreateArtistDTO toCreateDTO() {
        return CreateArtistDTO.builder()
            .artistId(artistId)
            .name(name)
            .artworkUrl(artworkUrl)
            .artworkWidth(artworkWidth)
            .artworkHeight(artworkHeight)
            .build();
    }
}
