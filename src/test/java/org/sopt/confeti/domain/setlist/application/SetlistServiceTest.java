package org.sopt.confeti.domain.setlist.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistAddMusicDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistCreateDTO;
import org.sopt.confeti.api.setlist.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.api.setlist.dto.response.SetlistSummaryDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistMusicRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SetlistServiceTest {

    @InjectMocks
    private SetlistService setlistService;

    @Mock
    private SetlistRepository setlistRepository;
    @Mock
    private ConcertRepository concertRepository;
    @Mock
    private FestivalRepository festivalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SetlistMusicRepository setlistMusicRepository;
    @Mock
    private S3FileHandler s3FileHandler;

    private final Long userId = 1L;
    private final User user = mockUser(userId);

    @BeforeEach
    void setup() throws Exception {
        lenient().when(s3FileHandler.getFileUrl(any(), any()))
                .thenReturn(new URL("https://mock-s3-url.com/poster.png"));
    }

    @Test
    void 셋리스트_전체조회_OLDEST() {
        Setlist concertSetlist = createSetlist(SetlistType.CONCERT, 100L);
        Setlist festivalSetlist = createSetlist(SetlistType.FESTIVAL, 200L);

        given(setlistRepository.findAllByUserId(userId)).willReturn(List.of(concertSetlist, festivalSetlist));
        given(concertRepository.findById(100L)).willReturn(
                Optional.of(mockConcert("IU 콘서트", LocalDate.of(2024, 11, 3))));
        given(festivalRepository.findById(200L)).willReturn(
                Optional.of(mockFestival("서울재즈페스티벌", LocalDate.of(2024, 5, 1))));

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
        given(concertRepository.findById(100L)).willReturn(
                Optional.of(mockConcert("IU 콘서트", LocalDate.of(2024, 11, 3))));
        given(festivalRepository.findById(200L)).willReturn(
                Optional.of(mockFestival("서울재즈페스티벌", LocalDate.of(2024, 5, 1))));

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

    @Test
    void 여러개의_공연을_셋리스트로_생성() {
        SetlistCreateDTO request1 = new SetlistCreateDTO(SetlistType.CONCERT, 100L);
        SetlistCreateDTO request2 = new SetlistCreateDTO(SetlistType.FESTIVAL, 200L);
        List<SetlistCreateDTO> requests = List.of(request1, request2);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Setlist savedSetlist1 = createSetlist(SetlistType.CONCERT, 100L);
        Setlist savedSetlist2 = createSetlist(SetlistType.FESTIVAL, 200L);
        ReflectionTestUtils.setField(savedSetlist1, "id", 1L);
        ReflectionTestUtils.setField(savedSetlist2, "id", 2L);

        given(setlistRepository.save(any(Setlist.class)))
                .willReturn(savedSetlist1)
                .willReturn(savedSetlist2);

        List<Long> result = setlistService.createSetLists(userId, requests);

        assertThat(result).hasSize(2).containsExactly(1L, 2L);
    }

    @Test
    void 이미_생성된_셋리스트는_중복_생성되지_않는다() {
        SetlistCreateDTO request1 = new SetlistCreateDTO(SetlistType.CONCERT, 100L);
        SetlistCreateDTO request2 = new SetlistCreateDTO(SetlistType.FESTIVAL, 200L);
        List<SetlistCreateDTO> requests = List.of(request1, request2);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(setlistRepository.existsByUserIdAndTypeAndTypeId(userId, SetlistType.CONCERT, 100L)).willReturn(true);
        given(setlistRepository.existsByUserIdAndTypeAndTypeId(userId, SetlistType.FESTIVAL, 200L)).willReturn(false);

        Setlist newSetlist = createSetlist(SetlistType.FESTIVAL, 200L);
        ReflectionTestUtils.setField(newSetlist, "id", 999L);
        given(setlistRepository.save(any(Setlist.class))).willReturn(newSetlist);

        List<Long> result = setlistService.createSetLists(userId, requests);

        assertThat(result).containsExactly(999L);
    }

    @Test
    void 셋리스트에_여러_곡을_추가하면_순서가_자동으로_부여된다() {
        Long setlistId = 100L;
        Setlist setlist = createSetlist(SetlistType.CONCERT, 1L);

        SetlistMusic existing1 = SetlistMusic.builder().artistName("EXO").trackName("Love Shot").orders(1).build();
        SetlistMusic existing2 = SetlistMusic.builder().artistName("BTS").trackName("Dynamite").orders(2).build();
        setlist.addMusics(existing1);
        setlist.addMusics(existing2);

        given(setlistRepository.findById(setlistId)).willReturn(Optional.of(setlist));

        List<SetlistAddMusicDTO> requests = List.of(
                new SetlistAddMusicDTO("01", "IU", "Love wins all", "url1", "preview1"),
                new SetlistAddMusicDTO("02", "NewJeans", "Hype Boy", "url2", "preview2")
        );

        int result = setlistService.addMusics(userId, setlistId, requests);

        assertThat(result).isEqualTo(2);
        assertThat(setlist.getMusics()).hasSize(4);
    }

    @Test
    void 셋리스트_상세조회_CONCERT_타입일_경우_정상조회() {
        Long setlistId = 10L;
        Long concertId = 100L;

        Setlist setlist = createSetlist(SetlistType.CONCERT, concertId);
        ReflectionTestUtils.setField(setlist, "id", setlistId);

        SetlistMusic music = createMusic("01", "IU", "Love wins all", 1);
        music.setSetlist(setlist);

        Concert concert = mockConcert("아이유 콘서트", LocalDate.of(2024, 5, 10));

        given(setlistRepository.findByIdAndUserId(setlistId, userId)).willReturn(Optional.of(setlist));
        given(concertRepository.findById(concertId)).willReturn(Optional.of(concert));
        given(setlistMusicRepository.findBySetlist(setlist)).willReturn(List.of(music));

        var result = setlistService.getSetlistDetail(userId, setlistId);

        assertThat(result.type()).isEqualTo("CONCERT");
        assertThat(result.posterUrl()).isEqualTo("https://mock-s3-url.com/poster.png");
    }

    @Test
    void 셋리스트_상세조회_FESTIVAL_타입일_경우_정상조회() {
        Long setlistId = 20L;
        Long festivalId = 200L;

        Setlist setlist = createSetlist(SetlistType.FESTIVAL, festivalId);
        ReflectionTestUtils.setField(setlist, "id", setlistId);

        SetlistMusic music = createMusic("02", "NewJeans", "ETA", 1);
        music.setSetlist(setlist);

        Festival festival = mockFestival("부산 록 페스티벌", LocalDate.of(2024, 8, 20));

        given(setlistRepository.findByIdAndUserId(setlistId, userId)).willReturn(Optional.of(setlist));
        given(festivalRepository.findById(festivalId)).willReturn(Optional.of(festival));
        given(setlistMusicRepository.findBySetlist(setlist)).willReturn(List.of(music));

        var result = setlistService.getSetlistDetail(userId, setlistId);

        assertThat(result.type()).isEqualTo("FESTIVAL");
        assertThat(result.posterUrl()).isEqualTo("https://mock-s3-url.com/poster.png");
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
        Concert concert = Concert.builder()
                .title(title).subtitle("sub")
                .startAt(endAt.minusDays(2)).endAt(endAt)
                .area("서울").posterPath(title + ".jpg")
                .reserveAt(LocalDateTime.now())
                .ageRating("ALL").time("18:00").price("10000").address("서울시")
                .artists(List.of()).reservationUrls(List.of())
                .build();
        ReflectionTestUtils.setField(concert, "id", 100L);
        return concert;
    }

    private Festival mockFestival(String title, LocalDate endAt) {
        Festival festival = Festival.builder()
                .title(title).subtitle("sub")
                .startAt(endAt.minusDays(2)).endAt(endAt)
                .area("부산").posterPath(title + ".jpg")
                .logoPath("logo.jpg").reserveAt(LocalDateTime.now())
                .ageRating("ALL").time("16:00")
                .price("8000").address("부산시").dates(List.of()).reservationUrls(List.of())
                .build();
        ReflectionTestUtils.setField(festival, "id", 200L);
        return festival;
    }

    private SetlistMusic createMusic(String trackId, String artistName, String trackName, int orders) {
        return SetlistMusic.builder()
                .trackId(trackId).artistName(artistName)
                .trackName(trackName).orders(orders)
                .build();
    }
}
