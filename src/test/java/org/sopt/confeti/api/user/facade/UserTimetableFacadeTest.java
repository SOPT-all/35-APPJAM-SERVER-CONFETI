package org.sopt.confeti.api.user.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.api.user.facade.dto.request.timetable.AddTimetableArtistDTO;
import org.sopt.confeti.api.user.facade.dto.request.timetable.AddTimetablesDTO;
import org.sopt.confeti.api.user.facade.dto.response.timetable.TimetableCreateResponseDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_date.application.FestivalDateService;
import org.sopt.confeti.domain.time_block.application.TimeBlockService;
import org.sopt.confeti.domain.timetable.Timetable;
import org.sopt.confeti.domain.timetable.application.TimetableService;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.interceptor.auth.UserInfo;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserTimetableFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private TimetableService timetableService;

    @Mock
    private FestivalService festivalService;

    @Mock
    private FestivalDateService festivalDateService;

    @Mock
    private TimeBlockService timeBlockService;

    @InjectMocks
    private UserTimetableFacade userTimetableFacade;

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void addTimetables_returnsCreatedIdsInRequestOrder() {
        UserContext.set(UserInfo.builder()
            .id(1L)
            .role(Role.GENERAL)
            .build());

        User user = User.builder()
            .provider(OAuthProvider.KAKAO)
            .socialId("social-id")
            .name("confeti")
            .profilePath(null)
            .role(Role.GENERAL)
            .build();

        Festival firstFestival = createFestival(1L, "First");
        Festival secondFestival = createFestival(2L, "Second");
        Timetable firstTimetable = createTimetable(101L, user, firstFestival);
        Timetable secondTimetable = createTimetable(202L, user, secondFestival);
        AddTimetablesDTO request = new AddTimetablesDTO(List.of(
            new AddTimetableArtistDTO(1L),
            new AddTimetableArtistDTO(2L)
        ));

        given(userService.findUserTimetablesById(1L)).willReturn(user);
        given(festivalService.findFestivalsByIdIn(List.of(1L, 2L))).willReturn(
            List.of(secondFestival, firstFestival));
        given(timetableService.addTimetables(eq(user), anyList())).willReturn(
            List.of(firstTimetable, secondTimetable));

        TimetableCreateResponseDTO response = userTimetableFacade.addTimetables(request);

        ArgumentCaptor<List<Festival>> festivalCaptor = ArgumentCaptor.forClass(List.class);
        verify(timetableService).addTimetables(eq(user), festivalCaptor.capture());
        verify(userService).updateHasTimetableHistory(1L);

        assertThat(festivalCaptor.getValue())
            .extracting(Festival::getId)
            .containsExactly(1L, 2L);
        assertThat(response.timetableIds()).containsExactly(101L, 202L);
    }

    private Festival createFestival(long id, String title) {
        Festival festival = Festival.create(
            title,
            LocalDate.of(2026, 3, 20),
            LocalDate.of(2026, 3, 21),
            "Seoul",
            "poster.png",
            null,
            "ALL",
            "120m",
            "100000",
            "address",
            TimetableSupportStatus.SUPPORTED,
            List.of(),
            List.of(),
            List.of()
        );
        ReflectionTestUtils.setField(festival, "id", id);
        return festival;
    }

    private Timetable createTimetable(long id, User user, Festival festival) {
        Timetable timetable = Timetable.create(user, festival);
        ReflectionTestUtils.setField(timetable, "id", id);
        return timetable;
    }
}
