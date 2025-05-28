package org.sopt.confeti.domain.setlist.application;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertArtistDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertDTO;
import org.sopt.confeti.api.dummy.facade.dto.concert.request.CreateConcertReservationUrlDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalArtistDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDateDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalReservationUrlDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalStageDTO;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalTimeDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.Default;

public class TestDataManager {

    public static final String 데이식스 = "1037939997";
    public static final String 아이묭 = "1165017710";
    public static final String 실리카겔 = "1031084591";
    public static final String 빅뱅 = "318754656";
    public static final String 잔나비 = "913424316";
    public static final String 로이킴 = "572430917";
    public static final String 창모 = "887777364";
    public static final String 우디 = "1277551001";
    public static final String 칸예 = "2715720";
    public static final String 빈지노 = "331767820";
    public static final String 리쌍 = "339950117";
    public static final String 리도어 = "1547913995";
    public static final String 한로로 = "1613668993";
    public static final String 터치드 = "1543982676";
    public static final String 오아시스 = "512633";
    public static final String 샤이니 = "433371033";
    public static final String 슈퍼주니어 = "284066214";
    public static final String 투애니원 = "329155759";
    public static final String wavetoearth = "1477295619";

    private static final List<CreateConcertDTO> createConcertDTOs = new ArrayList<>(
            Arrays.asList(
                    new CreateConcertDTO(
                            "종료된 콘서트 1 - 제목",
                            "종료된 콘서트 1 - 부제목",
                            LocalDate.now().minusDays(3),
                            LocalDate.now().minusDays(2),
                            "서울",
                            Default.IMG_PATH,
                            LocalDateTime.now().minusDays(4),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateConcertArtistDTO(데이식스),
                                    new CreateConcertArtistDTO(아이묭),
                                    new CreateConcertArtistDTO(실리카겔)
                            ),
                            Arrays.asList(
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            )
                    ),
                    new CreateConcertDTO(
                            "종료된 콘서트 2 - 제목",
                            "종료된 콘서트 2 - 부제목",
                            LocalDate.now().minusDays(4),
                            LocalDate.now().minusDays(3),
                            "서울",
                            Default.IMG_PATH,
                            LocalDateTime.now().minusDays(5),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateConcertArtistDTO(로이킴),
                                    new CreateConcertArtistDTO(창모)
                            ),
                            Arrays.asList(
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            )
                    ),
                    new CreateConcertDTO(
                            "expected 콘서트 1 - 제목",
                            "예정된 콘서트 1 - 부제목",
                            LocalDate.now().plusDays(2),
                            LocalDate.now().plusDays(4),
                            "서울",
                            Default.IMG_PATH,
                            LocalDateTime.now().plusDays(1),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateConcertArtistDTO(칸예),
                                    new CreateConcertArtistDTO(빈지노)
                            ),
                            Arrays.asList(
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            )
                    ),
                    new CreateConcertDTO(
                            "예정된 콘서트 2 - 제목",
                            "예정된 콘서트 2 - 부제목",
                            LocalDate.now().plusDays(4),
                            LocalDate.now().plusDays(7),
                            "서울",
                            Default.IMG_PATH,
                            LocalDateTime.now().plusDays(2),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateConcertArtistDTO(리도어)
                            ),
                            Arrays.asList(
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateConcertReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            )
                    )
            )
    );

    private static final List<CreateFestivalDTO> createFestivalDTOs = new ArrayList<>(
            Arrays.asList(
                    new CreateFestivalDTO(
                            "종료된 페스티벌 1 - 제목",
                            "종료된 페스티벌 1 - 부제목",
                            LocalDate.now().minusDays(3),
                            LocalDate.now().minusDays(1),
                            "서울",
                            Default.IMG_PATH,
                            Default.IMG_PATH,
                            LocalDateTime.now().minusDays(6),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            ),
                            Arrays.asList(
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().minusDays(3),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            빅뱅)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            우디)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            잔나비)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            리쌍)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    ),
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().minusDays(2),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            한로로)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            터치드)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            오아시스)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            샤이니)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    ),
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().minusDays(1),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            슈퍼주니어)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            투애니원)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            wavetoearth)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ),
                    new CreateFestivalDTO(
                            "종료된 페스티벌 2 - 제목",
                            "종료된 페스티벌 2 - 부제목",
                            LocalDate.now().minusDays(5),
                            LocalDate.now().minusDays(4),
                            "서울",
                            Default.IMG_PATH,
                            Default.IMG_PATH,
                            LocalDateTime.now().minusDays(15),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            ),
                            Arrays.asList(
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().minusDays(5),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(19, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            오아시스)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            창모)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            빈지노)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    ),
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().minusDays(4),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(16, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            슈퍼주니어)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(16, 0),
                                                                            LocalTime.of(20, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            샤이니)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            한로로)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            칸예)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ),
                    new CreateFestivalDTO(
                            "예정된 페스티벌 1 - 제목",
                            "예정된 페스티벌 1 - 부제목",
                            LocalDate.now().plusDays(4),
                            LocalDate.now().plusDays(4),
                            "서울",
                            Default.IMG_PATH,
                            Default.IMG_PATH,
                            LocalDateTime.now().plusDays(2),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            ),
                            Arrays.asList(
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().plusDays(4),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            로이킴)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            리도어)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(18, 0),
                                                                            LocalTime.of(23, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            데이식스),
                                                                                    new CreateFestivalArtistDTO(
                                                                                            칸예)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            실리카겔)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            리쌍)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 3",
                                                            3,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            아이묭)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            잔나비)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    ),
                    new CreateFestivalDTO(
                            "예정된 페스티벌 2 - 제목",
                            "예정된 페스티벌 2 - 부제목",
                            LocalDate.now().plusDays(10),
                            LocalDate.now().plusDays(11),
                            "서울",
                            Default.IMG_PATH,
                            Default.IMG_PATH,
                            LocalDateTime.now().plusDays(7),
                            "나이 제한",
                            "공연 시간",
                            "공연 가격표",
                            "공연 주소",
                            Arrays.asList(
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH),
                                    new CreateFestivalReservationUrlDTO("https://confeti.co.kr", "CONFETI",
                                            Default.IMG_PATH)
                            ),
                            Arrays.asList(
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().plusDays(10),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            빅뱅)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            우디)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            잔나비)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            리쌍)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    ),
                                    new CreateFestivalDateDTO(
                                            LocalDate.now().plusDays(11),
                                            LocalTime.of(11, 0),
                                            Arrays.asList(
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 1",
                                                            1,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            한로로)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            터치드)
                                                                            )
                                                                    )
                                                            )
                                                    ),
                                                    new CreateFestivalStageDTO(
                                                            "스테이지 2",
                                                            2,
                                                            Arrays.asList(
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(12, 0),
                                                                            LocalTime.of(15, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            wavetoearth)
                                                                            )
                                                                    ),
                                                                    new CreateFestivalTimeDTO(
                                                                            LocalTime.of(15, 0),
                                                                            LocalTime.of(18, 0),
                                                                            Arrays.asList(
                                                                                    new CreateFestivalArtistDTO(
                                                                                            투애니원)
                                                                            )
                                                                    )
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
    );

    public static List<Concert> createConcerts() {
        return createConcertDTOs.stream()
                .map(Concert::create)
                .toList();
    }

    public static List<Festival> createFestivals() {
        return createFestivalDTOs.stream()
                .map(Festival::create)
                .toList();
    }

    public static List<Performance> createPerformances() {
        return Stream.concat(
                IntStream.range(1, createConcertDTOs.size() + 1).mapToObj(i ->
                        Performance.create(i, createConcertDTOs.get(i - 1))
                ),
                IntStream.range(1, createFestivalDTOs.size() + 1).mapToObj(i ->
                        Performance.create(i, createFestivalDTOs.get(i - 1))
                )
        ).toList();
    }

    public static final List<String> searchTerms = new ArrayList<>(
            Arrays.asList(
                    "장범준", "장범준", "장범준", "장범준", "장범준",
                    "지디", "지디", "지디", "지디",
                    "아이유", "아이유", "아이유",
                    "데이식스", "데이식스",
                    "잔나비"
            )
    );

    public static final OAuthProvider userProvider = OAuthProvider.KAKAO;
    public static final String userSocialId = "99999999";

    public static User createUser() {
        return User.builder()
                .name("테스트 유저")
                .role(Role.GENERAL)
                .provider(userProvider)
                .socialId(userSocialId)
                .profilePath(Default.PROFILE_IMG_NAME)
                .build();
    }
}
