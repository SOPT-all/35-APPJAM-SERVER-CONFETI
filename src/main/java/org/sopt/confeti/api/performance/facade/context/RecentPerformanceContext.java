package org.sopt.confeti.api.performance.facade.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.domain.view.performance.Performance;

public class RecentPerformanceContext {

    public static final int MAX_SIZE = 7;

    private final List<RecentPerformanceDTO> performances = new ArrayList<>();
    private final Set<Long> addedPerformanceIds = new HashSet<>();

    public boolean isFull() {
        return performances.size() >= MAX_SIZE;
    }

    public int remainingSlots() {
        return MAX_SIZE - performances.size();
    }

    public void addFavoritePerformances(List<Performance> favoritePerformances) {
        for (Performance performance : favoritePerformances) {
            if (isFull()) {
                break;
            }
            if (addedPerformanceIds.add(performance.getId())) {
                performances.add(RecentPerformanceDTO.of(performance, true));
            }
        }
    }

    public void addGeneralPerformances(List<Performance> generalPerformances) {
        for (Performance performance : generalPerformances) {
            if (isFull()) {
                break;
            }
            if (addedPerformanceIds.add(performance.getId())) {
                performances.add(RecentPerformanceDTO.of(performance, false));
            }
        }
    }

    public Set<Long> getExcludedPerformanceIds() {
        return addedPerformanceIds.isEmpty() ? Set.of(-1L) : addedPerformanceIds;
    }

    public RecentPerformancesDTO build() {
        return new RecentPerformancesDTO(
            performances.stream()
                .sorted(Comparator.comparing(RecentPerformanceDTO::startAt))
                .toList()
        );
    }
}
