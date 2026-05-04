package org.sopt.confeti.api.performance.facade.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

public class RecentPerformanceContext {

    public static final int MAX_SIZE = 7;

    private final List<RecentPerformanceDTO> performances = new ArrayList<>();
    private final Set<PerformanceKey> addedPerformanceKeys = new HashSet<>();

    public boolean isFull() {
        return performances.size() >= MAX_SIZE;
    }

    public int remainingSlots() {
        return MAX_SIZE - performances.size();
    }

    public void addFavoritePerformances(List<PerformanceInfo> favoritePerformances) {
        for (PerformanceInfo performance : favoritePerformances) {
            if (isFull()) {
                break;
            }
            PerformanceKey key = new PerformanceKey(performance.type(), performance.typeId());
            if (addedPerformanceKeys.add(key)) {
                performances.add(RecentPerformanceDTO.of(performance, true));
            }
        }
    }

    public void addGeneralPerformances(List<PerformanceInfo> generalPerformances) {
        for (PerformanceInfo performance : generalPerformances) {
            if (isFull()) {
                break;
            }
            PerformanceKey key = new PerformanceKey(performance.type(), performance.typeId());
            if (addedPerformanceKeys.add(key)) {
                performances.add(RecentPerformanceDTO.of(performance, false));
            }
        }
    }

    public List<Long> getExcludedConcertIds() {
        List<Long> ids = addedPerformanceKeys.stream()
            .filter(key -> key.type() == PerformanceType.CONCERT)
            .map(PerformanceKey::id)
            .toList();
        return ids.isEmpty() ? List.of(-1L) : ids;
    }

    public List<Long> getExcludedFestivalIds() {
        List<Long> ids = addedPerformanceKeys.stream()
            .filter(key -> key.type() == PerformanceType.FESTIVAL)
            .map(PerformanceKey::id)
            .toList();
        return ids.isEmpty() ? List.of(-1L) : ids;
    }

    public RecentPerformancesDTO build() {
        return new RecentPerformancesDTO(
            performances.stream()
                .sorted(Comparator.comparing(RecentPerformanceDTO::startAt))
                .toList()
        );
    }
}
