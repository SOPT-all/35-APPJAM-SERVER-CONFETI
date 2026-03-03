package org.sopt.confeti.domain.performancedraft.application.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;

import java.time.LocalDateTime;

public record PerformanceDraftDto(
        Long id,
        PerformanceDraftType performanceType,
        DraftStatus status,
        String performanceData,
        String posterPath,
        String logoPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PerformanceDraftDto from(PerformanceDraft draft) {
        return new PerformanceDraftDto(
                draft.getId(),
                draft.getPerformanceType(),
                draft.getStatus(),
                draft.getPerformanceData(),
                draft.getPosterPath(),
                draft.getLogoPath(),
                draft.getCreatedAt(),
                draft.getUpdatedAt()
        );
    }

    public Set<String> getArtistIds(ObjectMapper objectMapper) {
        Set<String> artistIds = new HashSet<>();
        try {
            JsonNode root = objectMapper.readTree(performanceData);
            if (performanceType == PerformanceDraftType.CONCERT) {
                Optional.ofNullable(root.get("artists")).ifPresent(arr ->
                    arr.forEach(item -> Optional.ofNullable(item.get("artistId"))
                        .ifPresent(node -> artistIds.add(node.asText())))
                );
            } else {
                Optional.ofNullable(root.get("dates")).ifPresent(dates ->
                    dates.forEach(date -> Optional.ofNullable(date.get("dailyArtists")).ifPresent(daily ->
                        daily.forEach(item -> Optional.ofNullable(item.get("artistId"))
                            .ifPresent(node -> artistIds.add(node.asText())))
                    ))
                );
            }
        } catch (Exception e) {
            // 파싱 실패 시 빈 Set 반환
        }
        return artistIds;
    }
}
