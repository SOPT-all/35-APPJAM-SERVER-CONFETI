package org.sopt.confeti.domain.user_timetable.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableListDTO;
import org.sopt.confeti.domain.user_timetable.UserTimetable;
import org.sopt.confeti.domain.user_timetable.infra.repository.UserTimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTimetableService {

    private final UserTimetableRepository userTimetableRepository;

    @Transactional(readOnly = true)
    public List<UserTimetable> getUserTimeTables(long userId) {
        return userTimetableRepository.findByUserId(userId);
    }

    @Transactional
    public void patchTimetableFestival(List<UserTimetable> userTimetables, PatchTimetableDTO timetableDTO) {
        Map<Long, Boolean> updateMap = timetableDTO.userTimetables()
                .stream()
                .collect(Collectors.toMap(PatchTimetableListDTO::userTimetableId, PatchTimetableListDTO::isSelected));

        for (UserTimetable timetable : userTimetables) {
            Boolean isSelected = updateMap.get(timetable.getId());
            if (isSelected != null) {
                timetable.setSelected(isSelected);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<UserTimetable> getUserTimetablesByFestivalTimeId(final long userId, final List<Long> festivalTimeIds) {
        return userTimetableRepository.findByUserIdAndFestivalTimeIds(userId, festivalTimeIds);
    }
}
