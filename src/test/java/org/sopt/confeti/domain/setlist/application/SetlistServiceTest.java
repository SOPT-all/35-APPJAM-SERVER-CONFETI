package org.sopt.confeti.domain.setlist.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.setlist.*;
import org.sopt.confeti.domain.setlist.application.dto.request.AddSetListMusicRequest;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistCreateRequest;
import org.sopt.confeti.domain.setlist.application.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.SetlistSummaryDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SetlistServiceTest {

    @InjectMocks
    private SetlistService setlistService;

    @Mock private SetlistRepository setlistRepository;
    @Mock private ConcertRepository concertRepository;
    @Mock private FestivalRepository festivalRepository;
    @Mock private UserRepository userRepository;

    private final Long userId = 1L;
    private final User user = mockUser(userId);

    @Test
    void 셋리스트_전체조회_OLDEST() {
        Setlist concertSetlist = createSetlist(SetlistType.CONCERT, 100L);
        Setlist festivalSetlist = createSetlist(SetlistType.FESTIVAL, 200L);

        given(setlistRepository.findAllByUserId(userId)).willReturn(List.of(concertSetlist, festivalSetlist));
        given(concertRepository.findById(100L)).willReturn(Optional.of(mockConcert("IU 콘서트", LocalDate.of(2024, 11, 3))));
        given(festivalRepository.findById(200L)).willReturn(Optional.of(mockFestival("서울재즈페스티벌", LocalDate.of(2024, 5, 1))));

        GetAllSetlistsResponse result = setlistService.getAllMySetlists(userId, SetlistSortType.OLDEST);

        assertThat(result.totalCount()).isEqualTo(2);
        assertThat(result.setlists()).extracting(SetlistSummaryDto::title)
                .containsExactly("서울재즈페스티벌", "IU 콘서트");
    }

    @Test
    void 셋리스트_전체조회_LATEST() {
        Setlist concertSetlist = createSetlist(SetlistType.CONCERT, 100L);
        Setlist festivalSetlist = createSetlist(SetlistType.FESTIVAL, 200L);

        given(setlistRepository.findAllByUserId(userId)).willReturn(List.of(concertSetlist, festivalSetlist));
        given(concertRepository.findById(100L)).willReturn(Optional.of(mockConcert("IU 콘서트", LocalDate.of(2024, 11, 3))));
        given(festivalRepository.findById(200L)).willReturn(Optional.of(mockFestival("서울재즈페스티벌", LocalDate.of(2024, 5, 1))));

        GetAllSetlistsResponse result = setlistService.getAllMySetlists(userId, SetlistSortType.LATEST);

        assertThat(result.totalCount()).isEqualTo(2);
        assertThat(result.setlists()).extracting(SetlistSummaryDto::title)
                .containsExactly("IU 콘서트", "서울재즈페스티벌");
    }

    @Test
    void 셋리스트_미리보기_최대_3개까지만_오래된_순으로_반환() {
        List<Setlist> setlists = List.of(
                createSetlist(SetlistType.FESTIVAL, 201L),
                createSetlist(SetlistType.FESTIVAL, 200L),
                createSetlist(SetlistType.CONCERT, 100L),
                createSetlist(SetlistType.CONCERT, 101L),
                createSetlist(SetlistType.CONCERT, 102L)
        );

        given(setlistRepository.findAllByUserId(userId)).willReturn(setlists);
        given(festivalRepository.findById(201L)).willReturn(Optional.of(mockFestival("F2", LocalDate.of(2024, 3, 1))));
        given(festivalRepository.findById(200L)).willReturn(Optional.of(mockFestival("F1", LocalDate.of(2024, 4, 1))));
        given(concertRepository.findById(100L)).willReturn(Optional.of(mockConcert("C1", LocalDate.of(2024, 5, 1))));
        given(concertRepository.findById(101L)).willReturn(Optional.of(mockConcert("C2", LocalDate.of(2024, 6, 1))));
        given(concertRepository.findById(102L)).willReturn(Optional.of(mockConcert("C3", LocalDate.of(2024, 7, 1))));

        List<SetlistSummaryDto> result = setlistService.getPreviewMySetlists(userId);

        assertThat(result).hasSize(3);
        assertThat(result).extracting(SetlistSummaryDto::title).containsExactly("F2", "F1", "C1");
    }

    private static User mockUser(Long id) {
        User user = User.builder()
                .provider(OAuthProvider.KAKAO)
                .socialId("mock")
                .name("mockUser")
                .profilePath("profile.jpg")
                .role(Role.GENERAL)
                .build();
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }

    private Setlist createSetlist(SetlistType type, Long typeId) {
        return Setlist.builder().user(user).type(type).typeId(typeId).build();
    }

    private Concert mockConcert(String title, LocalDate endAt) {
        return Concert.builder()
                .title(title)
                .subtitle("sub")
                .startAt(endAt.minusDays(2))
                .endAt(endAt)
                .area("서울")
                .posterPath(title + ".jpg")
                .posterBgPath("bg.jpg")
                .concertInfoImgPath("info.jpg")
                .reserveAt(LocalDateTime.now())
                .reservationUrl("url")
                .reservationOffice("office")
                .ageRating("ALL")
                .time("18:00")
                .price("10000")
                .address("서울시")
                .artists(List.of())
                .musics(List.of())
                .reservationUrls(List.of())
                .build();
    }

    private Festival mockFestival(String title, LocalDate endAt) {
        return Festival.builder()
                .title(title)
                .subtitle("sub")
                .startAt(endAt.minusDays(2))
                .endAt(endAt)
                .area("부산")
                .posterPath(title + ".jpg")
                .posterBgPath("bg.jpg")
                .festivalInfoImgPath("info.jpg")
                .logoPath("logo.jpg")
                .reserveAt(LocalDateTime.now())
                .reservationUrl("url")
                .reservationOffice("office")
                .ageRating("ALL")
                .time("16:00")
                .price("8000")
                .address("부산시")
                .dates(List.of())
                .musics(List.of())
                .reservationUrls(List.of())
                .build();
    }

    @Test
    void 여러개의_공연을_셋리스트로_생성() {
        // given
        SetlistCreateRequest request1 = new SetlistCreateRequest(SetlistType.CONCERT, 100L);
        SetlistCreateRequest request2 = new SetlistCreateRequest(SetlistType.FESTIVAL, 200L);
        List<SetlistCreateRequest> requests = List.of(request1, request2);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Setlist savedSetlist1 = createSetlist(SetlistType.CONCERT, 100L);
        Setlist savedSetlist2 = createSetlist(SetlistType.FESTIVAL, 200L);
        ReflectionTestUtils.setField(savedSetlist1, "id", 1L);
        ReflectionTestUtils.setField(savedSetlist2, "id", 2L);

        given(setlistRepository.save(any(Setlist.class)))
                .willReturn(savedSetlist1)
                .willReturn(savedSetlist2);

        // when
        List<Long> result = setlistService.createSetLists(userId, requests);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(1L, 2L);
    }

    @Test
    void 이미_생성된_셋리스트는_중복_생성되지_않는다() {
        // given
        SetlistCreateRequest request1 = new SetlistCreateRequest(SetlistType.CONCERT, 100L);
        SetlistCreateRequest request2 = new SetlistCreateRequest(SetlistType.FESTIVAL, 200L);
        List<SetlistCreateRequest> requests = List.of(request1, request2);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(setlistRepository.existsByUserIdAndTypeAndTypeId(userId, SetlistType.CONCERT, 100L)).willReturn(true); // 이미 존재
        given(setlistRepository.existsByUserIdAndTypeAndTypeId(userId, SetlistType.FESTIVAL, 200L)).willReturn(false);

        Setlist newSetlist = createSetlist(SetlistType.FESTIVAL, 200L);
        ReflectionTestUtils.setField(newSetlist, "id", 999L);
        given(setlistRepository.save(any(Setlist.class))).willReturn(newSetlist);

        // when
        List<Long> result = setlistService.createSetLists(userId, requests);

        // then
        assertThat(result).containsExactly(999L);
    }

    @Test
    void 셋리스트에_여러_곡을_추가하면_순서가_자동으로_부여된다() {
        // given
        Long setlistId = 100L;
        Setlist setlist = Setlist.builder()
                .user(user)
                .type(SetlistType.CONCERT)
                .typeId(1L)
                .build();

        SetlistMusic existing1 = SetlistMusic.builder()
                .artistName("EXO").trackName("Love Shot").orders(1).build();
        SetlistMusic existing2 = SetlistMusic.builder()
                .artistName("BTS").trackName("Dynamite").orders(2).build();
        setlist.addMusics(existing1);
        setlist.addMusics(existing2);

        given(setlistRepository.findById(setlistId)).willReturn(Optional.of(setlist));

        List<AddSetListMusicRequest> requests = List.of(
                new AddSetListMusicRequest("01", "IU", "Love wins all", "url1", "preview1"),
                new AddSetListMusicRequest("02", "NewJeans", "Hype Boy", "url2", "preview2")
        );

        // when
        int result = setlistService.addMusics(userId, setlistId, requests);

        // then
        assertThat(result).isEqualTo(2);
        List<SetlistMusic> allMusics = setlist.getMusics();
        assertThat(allMusics).hasSize(4);
        assertThat(allMusics.get(2).getOrders()).isEqualTo(3);
        assertThat(allMusics.get(2).getArtistName()).isEqualTo("IU");
        assertThat(allMusics.get(3).getOrders()).isEqualTo(4);
        assertThat(allMusics.get(3).getArtistName()).isEqualTo("NewJeans");
    }


}
