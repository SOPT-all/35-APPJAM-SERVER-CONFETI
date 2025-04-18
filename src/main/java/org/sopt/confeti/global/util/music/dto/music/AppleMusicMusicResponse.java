package org.sopt.confeti.global.util.music.dto.music;

public record AppleMusicMusicResponse(
        String id,
        String type,
        AppleMusicMusicAttributesResponse attributes,
        AppleMusicMusicRelationshipsResponse relationships
) {
}
