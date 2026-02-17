package org.sopt.confeti.domain.time_block.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.timetable.PatchTimeBlockDTO;
import org.sopt.confeti.api.user.facade.dto.request.timetable.PatchTimeBlocksDTO;
import org.sopt.confeti.domain.time_block.TimeBlock;
import org.sopt.confeti.domain.time_block.infra.repository.TimeBlockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TimeBlockService {

    private final TimeBlockRepository timeBlockRepository;

    @Transactional(readOnly = true)
    public List<TimeBlock> getTimeBlocks(long userId) {
        return timeBlockRepository.findByUserId(userId);
    }

    @Transactional
    public void patchTimeBlocks(List<TimeBlock> timeBlocks, PatchTimeBlocksDTO timeBlockDTO) {
        Map<Long, Boolean> updateMap = timeBlockDTO.timeBlocks()
            .stream()
            .collect(
                Collectors.toMap(PatchTimeBlockDTO::timeBlockId, PatchTimeBlockDTO::isSelected));

        for (TimeBlock timeBlock : timeBlocks) {
            Boolean isSelected = updateMap.get(timeBlock.getId());
            if (isSelected != null) {
                timeBlock.setSelected(isSelected);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<TimeBlock> getTimeBlocksByFestivalTimeId(final long userId,
        final List<Long> festivalTimeIds) {
        return timeBlockRepository.findByUserIdAndFestivalTimeIds(userId, festivalTimeIds);
    }
}
