package org.sopt.confeti.api.performance.facade.context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDetailDTO;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public class PerformanceReservationContext {

    public static final int MAX_SIZE = 5;

    private final List<PerformanceReservationDetailDTO> performances = new ArrayList<>();
    private final Set<String> addedKeys = new HashSet<>();

    public boolean isFull() {
        return performances.size() >= MAX_SIZE;
    }

    public int remainingSlots() {
        return MAX_SIZE - performances.size();
    }

    public void addFavoritePerformances(List<PerformanceTicketDTO> tickets) {
        for (PerformanceTicketDTO ticket : tickets) {
            if (isFull()) {
                break;
            }
            String key = ticket.type().name() + "_" + ticket.typeId();
            if (addedKeys.add(key)) {
                performances.add(PerformanceReservationDetailDTO.of(ticket, true));
            }
        }
    }

    public void addGeneralPerformances(List<PerformanceTicketDTO> tickets) {
        for (PerformanceTicketDTO ticket : tickets) {
            if (isFull()) {
                break;
            }
            String key = ticket.type().name() + "_" + ticket.typeId();
            if (addedKeys.add(key)) {
                performances.add(PerformanceReservationDetailDTO.of(ticket, false));
            }
        }
    }

    public List<Long> getExcludedConcertIds() {
        List<Long> ids = addedKeys.stream()
            .filter(key -> key.startsWith(PerformanceType.CONCERT.name() + "_"))
            .map(key -> Long.parseLong(key.substring(key.indexOf("_") + 1)))
            .toList();
        return ids.isEmpty() ? List.of(-1L) : ids;
    }

    public List<Long> getExcludedFestivalIds() {
        List<Long> ids = addedKeys.stream()
            .filter(key -> key.startsWith(PerformanceType.FESTIVAL.name() + "_"))
            .map(key -> Long.parseLong(key.substring(key.indexOf("_") + 1)))
            .toList();
        return ids.isEmpty() ? List.of(-1L) : ids;
    }

    public PerformanceReservationDTO build() {
        List<PerformanceReservationDetailDTO> performancesWithIndex = new ArrayList<>();
        for (int i = 0; i < performances.size(); i++) {
            performancesWithIndex.add(performances.get(i).withIndex(i + 1));
        }

        return new PerformanceReservationDTO(
            performancesWithIndex.stream()
                .sorted(Comparator.comparing(PerformanceReservationDetailDTO::reserveAt))
                .toList()
        );
    }
}
