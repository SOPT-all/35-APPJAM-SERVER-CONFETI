package org.sopt.confeti.domain.setlist.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.LocalDate;
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
import org.sopt.confeti.domain.setlist.application.dto.response.GetAllSetlistsResponse;
import org.sopt.confeti.domain.setlist.application.dto.response.SetlistSummaryDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.constant.Role;
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

    @Test
    void 셋리스트_전체조회_OLDEST() {
        // given
        Long userId = 1L;
        User mockUser = User.builder()
                .provider(OAuthProvider.KAKAO)
                .socialId("kakao-123")
                .name("테스트 유저")
                .profilePath("profile.jpg")
                .role(Role.GENERAL)
                .build();
        ReflectionTestUtils.setField(mockUser, "id", userId);

        Setlist concertSetlist = Setlist.builder()
                .user(mockUser)
                .type(SetlistType.CONCERT)
                .typeId(100L)
                .build();

        Setlist festivalSetlist = Setlist.builder()
                .user(mockUser)
                .type(SetlistType.FESTIVAL)
                .typeId(200L)
                .build();

        Concert concert = Concert.builder()
                .title("IU 콘서트")
                .subtitle("Love Poem")
                .startAt(LocalDate.of(2024, 11, 1))
                .endAt(LocalDate.of(2024, 11, 3))
                .area("서울")
                .posterPath("iu.jpg")
                .posterBgPath("bg.jpg")
                .concertInfoImgPath("info.jpg")
                .reserveAt(LocalDate.now().atStartOfDay())
                .reservationUrl("url")
                .reservationOffice("office")
                .ageRating("전체관람가")
                .time("18:00")
                .price("99,000원")
                .address("서울 올림픽공원")
                .artists(List.of())
                .musics(List.of())
                .reservationUrls(List.of())
                .build();

        Festival festival = Festival.builder()
                .title("서울재즈페스티벌")
                .subtitle("2024 Edition")
                .startAt(LocalDate.of(2024, 4, 28))
                .endAt(LocalDate.of(2024, 5, 1))
                .area("서울")
                .posterPath("jazz.jpg")
                .posterBgPath("bg.jpg")
                .festivalInfoImgPath("info.jpg")
                .logoPath("logo.jpg")
                .reserveAt(LocalDate.now().atStartOfDay())
                .reservationUrl("url")
                .reservationOffice("office")
                .ageRating("19세 이상")
                .time("14:00")
                .price("79,000원")
                .address("서울 난지공원")
                .dates(List.of())
                .musics(List.of())
                .reservationUrls(List.of())
                .build();

        given(setlistRepository.findAllByUserId(userId)).willReturn(List.of(concertSetlist, festivalSetlist));
        given(concertRepository.findById(100L)).willReturn(Optional.of(concert));
        given(festivalRepository.findById(200L)).willReturn(Optional.of(festival));

        // when
        GetAllSetlistsResponse response = setlistService.getAllMySetlists(userId, SetlistSortType.OLDEST);

        // then
        assertThat(response.totalCount()).isEqualTo(2);
        assertThat(response.setlists())
                .extracting(SetlistSummaryDto::title)
                .containsExactly("서울재즈페스티벌", "IU 콘서트"); // endAt 기준 오름차순 정렬
    }

    @Test
    void 셋리스트_전체조회_LATEST() {
        // given
        Long userId = 1L;
        User mockUser = User.builder()
                .provider(OAuthProvider.KAKAO)
                .socialId("kakao-123")
                .name("테스트 유저")
                .profilePath("profile.jpg")
                .role(Role.GENERAL)
                .build();
        ReflectionTestUtils.setField(mockUser, "id", userId);

        Setlist concertSetlist = Setlist.builder()
                .user(mockUser)
                .type(SetlistType.CONCERT)
                .typeId(100L)
                .build();

        Setlist festivalSetlist = Setlist.builder()
                .user(mockUser)
                .type(SetlistType.FESTIVAL)
                .typeId(200L)
                .build();

        Concert concert = Concert.builder()
                .title("IU 콘서트")
                .subtitle("Love Poem")
                .startAt(LocalDate.of(2024, 11, 1))
                .endAt(LocalDate.of(2024, 11, 3))
                .area("서울")
                .posterPath("iu.jpg")
                .posterBgPath("bg.jpg")
                .concertInfoImgPath("info.jpg")
                .reserveAt(LocalDate.now().atStartOfDay())
                .reservationUrl("url")
                .reservationOffice("office")
                .ageRating("전체관람가")
                .time("18:00")
                .price("99,000원")
                .address("서울 올림픽공원")
                .artists(List.of())
                .musics(List.of())
                .reservationUrls(List.of())
                .build();

        Festival festival = Festival.builder()
                .title("서울재즈페스티벌")
                .subtitle("2024 Edition")
                .startAt(LocalDate.of(2024, 4, 28))
                .endAt(LocalDate.of(2024, 5, 1))
                .area("서울")
                .posterPath("jazz.jpg")
                .posterBgPath("bg.jpg")
                .festivalInfoImgPath("info.jpg")
                .logoPath("logo.jpg")
                .reserveAt(LocalDate.now().atStartOfDay())
                .reservationUrl("url")
                .reservationOffice("office")
                .ageRating("19세 이상")
                .time("14:00")
                .price("79,000원")
                .address("서울 난지공원")
                .dates(List.of())
                .musics(List.of())
                .reservationUrls(List.of())
                .build();

        given(setlistRepository.findAllByUserId(userId)).willReturn(List.of(concertSetlist, festivalSetlist));
        given(concertRepository.findById(100L)).willReturn(Optional.of(concert));
        given(festivalRepository.findById(200L)).willReturn(Optional.of(festival));

        // when
        GetAllSetlistsResponse response = setlistService.getAllMySetlists(userId, SetlistSortType.LATEST);

        // then
        assertThat(response.totalCount()).isEqualTo(2);
        assertThat(response.setlists())
                .extracting(SetlistSummaryDto::title)
                .containsExactly("IU 콘서트", "서울재즈페스티벌"); // endAt 기준 내림차순 정렬
    }
}
