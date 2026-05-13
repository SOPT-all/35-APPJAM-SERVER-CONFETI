package org.sopt.confeti.api.admin.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.api.admin.dto.response.PutAdminConcertResponse;
import org.sopt.confeti.api.admin.dto.response.PutAdminFestivalResponse;
import org.sopt.confeti.api.admin.facade.dto.request.AdminConcertCommand;
import org.sopt.confeti.api.admin.facade.dto.request.AdminFestivalCommand;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.concert_reservation_schedule.ConcertReservationSchedule;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival_reservation_schedule.FestivalReservationSchedule;
import org.sopt.confeti.domain.music.artist.application.ArtistMusicAPIService;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftParser;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftService;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.transaction.TxRunner;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@ResourceLock("Tx.txRunner")
class AdminFacadeUpdateTest {

    @Mock
    private TicketVendorService ticketVendorService;
    @Mock
    private ConcertService concertService;
    @Mock
    private FestivalService festivalService;
    @Mock
    private ConcertFavoriteService concertFavoriteService;
    @Mock
    private SetlistService setlistService;
    @Mock
    private MusicAPIHandler musicAPIHandler;
    @Mock
    private S3FileHandler s3FileHandler;
    @Mock
    private PerformanceDraftService performanceDraftService;
    @Mock
    private ArtistService artistService;
    @Mock
    private PerformanceService performanceService;
    @Mock
    private PerformanceSearchService performanceSearchService;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private PerformanceDraftParser performanceDraftParser;
    @Mock
    private ArtistMusicAPIService artistMusicAPIService;
    @Mock
    private MultipartFile poster;

    private AdminFacade adminFacade;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(Tx.class, "txRunner", new TxRunner());
        adminFacade = new AdminFacade(
            ticketVendorService,
            concertService,
            festivalService,
            concertFavoriteService,
            setlistService,
            musicAPIHandler,
            s3FileHandler,
            performanceDraftService,
            artistService,
            performanceService,
            performanceSearchService,
            eventPublisher,
            performanceDraftParser,
            artistMusicAPIService
        );
    }

    @AfterEach
    void tearDown() {
        ReflectionTestUtils.setField(Tx.class, "txRunner", null);
    }

    @Test
    void upsertFestival_수정시_상세_캐시를_비우고_예약일정을_갱신한다() {
        long festivalId = 55L;
        Festival festival = Festival.create(
            "PEAK FESTIVAL 2026",
            LocalDate.of(2026, 5, 23),
            LocalDate.of(2026, 5, 24),
            "난지한강공원",
            "poster.png",
            "logo.png",
            "전체 관람가",
            "540분",
            "일일권 110,000원 / 양일권 149,000원",
            "서울특별시 마포구 한강난지로 162(상암동) 난지한강공원",
            TimetableSupportStatus.NOT_SUPPORTED,
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(List.of(
                FestivalReservationSchedule.create(
                    "1차",
                    LocalDateTime.of(2026, 3, 4, 18, 0)
                )
            ))
        );
        Performance performance = Performance.create(
            festivalId,
            festivalUpdateCommand(festivalId),
            "poster.png",
            new ArrayList<>()
        );

        given(festivalService.getWithRelationsById(festivalId)).willReturn(festival);
        given(performanceService.getWithArtistsByTypeAndTypeId(PerformanceType.FESTIVAL, festivalId))
            .willReturn(performance);

        PutAdminFestivalResponse response = adminFacade.upsertFestival(
            null,
            null,
            festivalUpdateCommand(festivalId)
        );

        assertThat(response.festivalId()).isEqualTo(festivalId);
        assertThat(festival.getReservationSchedules())
            .extracting(FestivalReservationSchedule::getRoundName)
            .containsExactly("1차", "2차", "휠체어석");
        verify(festivalService).deleteDetailCache(festivalId);
    }

    @Test
    void upsertConcert_수정시_상세_캐시를_비우고_예약일정을_갱신한다() {
        long concertId = 77L;
        Concert concert = Concert.create(
            "SUMMER SONIC 2026",
            LocalDate.of(2026, 8, 15),
            LocalDate.of(2026, 8, 15),
            "KSPO DOME",
            "old-poster.png",
            "전체 관람가",
            "180분",
            "132,000원",
            "서울특별시 송파구 올림픽로 424",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(List.of(
                ConcertReservationSchedule.create(
                    "1차",
                    LocalDateTime.of(2026, 6, 1, 20, 0)
                )
            ))
        );
        Performance performance = Performance.createConcert(
            concertId,
            "SUMMER SONIC 2026",
            "KSPO DOME",
            LocalDate.of(2026, 8, 15),
            LocalDate.of(2026, 8, 15),
            "old-poster.png",
            new ArrayList<>()
        );

        given(s3FileHandler.uploadFile(poster, "concert/poster/")).willReturn("new-poster.png");
        given(concertService.findWithRelationsById(concertId)).willReturn(concert);
        given(performanceService.getPerformanceByTypeAndTypeId(PerformanceType.CONCERT, concertId))
            .willReturn(performance);

        PutAdminConcertResponse response = adminFacade.upsertConcert(
            poster,
            concertUpdateCommand(concertId)
        );

        assertThat(response.concertId()).isEqualTo(concertId);
        assertThat(concert.getReservationSchedules())
            .extracting(ConcertReservationSchedule::getRoundName)
            .containsExactly("1차", "2차");
        verify(concertService).deleteDetailCache(concertId);
    }

    @Test
    void upsertConcert_캐시무효화에_실패해도_S3_롤백없이_응답한다() {
        long concertId = 78L;
        Concert concert = Concert.create(
            "SUMMER SONIC 2026",
            LocalDate.of(2026, 8, 15),
            LocalDate.of(2026, 8, 15),
            "KSPO DOME",
            "old-poster.png",
            "전체 관람가",
            "180분",
            "132,000원",
            "서울특별시 송파구 올림픽로 424",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(List.of(
                ConcertReservationSchedule.create(
                    "1차",
                    LocalDateTime.of(2026, 6, 1, 20, 0)
                )
            ))
        );
        Performance performance = Performance.createConcert(
            concertId,
            "SUMMER SONIC 2026",
            "KSPO DOME",
            LocalDate.of(2026, 8, 15),
            LocalDate.of(2026, 8, 15),
            "old-poster.png",
            new ArrayList<>()
        );

        given(s3FileHandler.uploadFile(poster, "concert/poster/")).willReturn("new-poster.png");
        given(concertService.findWithRelationsById(concertId)).willReturn(concert);
        given(performanceService.getPerformanceByTypeAndTypeId(PerformanceType.CONCERT, concertId))
            .willReturn(performance);
        org.mockito.Mockito.doThrow(new RuntimeException("redis down"))
            .when(concertService).deleteDetailCache(concertId);

        PutAdminConcertResponse response = adminFacade.upsertConcert(
            poster,
            concertUpdateCommand(concertId)
        );

        assertThat(response.concertId()).isEqualTo(concertId);
        verify(eventPublisher, never()).publishEvent(org.mockito.ArgumentMatchers.<Object>argThat(
            event -> event instanceof org.sopt.confeti.global.event.S3FileDeleteEvent s3Event
                && "new-poster.png".equals(s3Event.fullPath())
        ));
    }

    private AdminFestivalCommand festivalUpdateCommand(long festivalId) {
        return AdminFestivalCommand.of(
            festivalId,
            "PEAK FESTIVAL 2026",
            LocalDate.of(2026, 5, 23),
            LocalDate.of(2026, 5, 24),
            "난지한강공원",
            "전체 관람가",
            "540분",
            "일일권 110,000원 / 양일권 149,000원",
            "서울특별시 마포구 한강난지로 162(상암동) 난지한강공원",
            TimetableSupportStatus.NOT_SUPPORTED,
            List.of(),
            List.of(
                AdminFestivalCommand.ReservationScheduleCommand.of(
                    "1차",
                    LocalDateTime.of(2026, 3, 4, 18, 0)
                ),
                AdminFestivalCommand.ReservationScheduleCommand.of(
                    "2차",
                    LocalDateTime.of(2026, 4, 23, 14, 0)
                ),
                AdminFestivalCommand.ReservationScheduleCommand.of(
                    "휠체어석",
                    LocalDateTime.of(2026, 4, 24, 16, 49)
                )
            ),
            List.of()
        );
    }

    private AdminConcertCommand concertUpdateCommand(long concertId) {
        return AdminConcertCommand.of(
            concertId,
            "SUMMER SONIC 2026",
            LocalDate.of(2026, 8, 15),
            LocalDate.of(2026, 8, 15),
            "KSPO DOME",
            "전체 관람가",
            "180분",
            "132,000원",
            "서울특별시 송파구 올림픽로 424",
            List.of(),
            List.of(),
            List.of(
                AdminConcertCommand.ReservationScheduleCommand.of(
                    "1차",
                    LocalDateTime.of(2026, 6, 1, 20, 0)
                ),
                AdminConcertCommand.ReservationScheduleCommand.of(
                    "2차",
                    LocalDateTime.of(2026, 6, 15, 20, 0)
                )
            )
        );
    }
}
