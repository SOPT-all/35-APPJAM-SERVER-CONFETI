package org.sopt.confeti.global.util.music.dto.artist;

public record AppleMusicArtistResponse(
        String id,
        String type,
        AppleMusicArtistAttributesResponse attributes,
        AppleMusicArtistRelationshipsResponse relationships
) {
}
