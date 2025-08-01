package org.sopt.confeti.domain.user_timetable.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableListDTO;
import org.sopt.confeti.domain.user_timetable.UserTimetable_DEPRECATED;
import org.sopt.confeti.domain.user_timetable.infra.repository.UserTimetableRepository_DEPRECATED;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTimetableService_DEPRECATED {

    private final UserTimetableRepository_DEPRECATED userTimetableRepositoryDEPRECATED;

    @Transactional(readOnly = true)
    public List<UserTimetable_DEPRECATED> getUserTimeTables(long userId) {
        return userTimetableRepositoryDEPRECATED.findByUserId(userId);
    }

    @Transactional
    public void patchTimetableFestival(List<UserTimetable_DEPRECATED> userTimetableDEPRECATEDS, PatchTimetableDTO timetableDTO) {
        Map<Long, Boolean> updateMap = timetableDTO.userTimetables()
                .stream()
                .collect(Collectors.toMap(PatchTimetableListDTO::userTimetableId, PatchTimetableListDTO::isSelected));

        for (UserTimetable_DEPRECATED timetable : userTimetableDEPRECATEDS) {
            Boolean isSelected = updateMap.get(timetable.getId());
            if (isSelected != null) {
                timetable.setSelected(isSelected);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<UserTimetable_DEPRECATED> getUserTimetablesByFestivalTimeId(final long userId, final List<Long> festivalTimeIds) {
        return userTimetableRepositoryDEPRECATED.findByUserIdAndFestivalTimeIds(userId, festivalTimeIds);
    }
}
