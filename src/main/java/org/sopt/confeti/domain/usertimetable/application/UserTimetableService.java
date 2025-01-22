package org.sopt.confeti.domain.usertimetable.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableDTO;
import org.sopt.confeti.api.user.facade.dto.request.PatchTimetableListDTO;
import org.sopt.confeti.domain.usertimetable.UserTimetable;
import org.sopt.confeti.domain.usertimetable.infra.repository.UserTimetableRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserTimetableService {

    private final UserTimetableRepository userTimetableRepository;

    @Transactional
    public void patchTimetableFestival(long userId, PatchTimetableDTO timetableDTO) {
        List<UserTimetable> userTimetables = userTimetableRepository.findByUserId(userId);

        if (userTimetables.isEmpty()) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        Map<Long, Boolean> updateMap = timetableDTO.userTimetables()
                .stream()
                .collect(Collectors.toMap(PatchTimetableListDTO::userTimetableId, PatchTimetableListDTO::isSelected));

        for (UserTimetable timetable : userTimetables) {
            Boolean isSelected = updateMap.get(timetable.getId());
            if (isSelected != null) {
                timetable.setIsSelected(isSelected);
            }
        }

        userTimetableRepository.saveAll(userTimetables);
    }
}
