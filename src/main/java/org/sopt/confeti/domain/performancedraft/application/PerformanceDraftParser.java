package org.sopt.confeti.domain.performancedraft.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PerformanceDraftParser {

    private final ObjectMapper objectMapper;

    public Set<String> parseArtistIds(PerformanceDraftType performanceDraftType, String performanceData) {
        Set<String> artistIds = new HashSet<>();
        try {
            JsonNode root = objectMapper.readTree(performanceData);
            if (performanceDraftType == PerformanceDraftType.CONCERT) {
                Optional.ofNullable(root.get("artists")).ifPresent(arr ->
                    arr.forEach(item -> Optional.ofNullable(item.get("artistId"))
                        .ifPresent(node -> artistIds.add(node.asText())))
                );
            } else {
                Optional.ofNullable(root.get("dates")).ifPresent(dates ->
                    dates.forEach(date -> Optional.ofNullable(date.get("dailyArtists"))
                        .ifPresent(daily -> daily.forEach(item -> Optional.ofNullable(item.get("artistId"))
                            .ifPresent(node -> artistIds.add(node.asText())))))
                );
            }
        } catch (Exception e) {
            // 파싱 실패 시 빈 Set 반환
        }
        return artistIds;
    }


}
