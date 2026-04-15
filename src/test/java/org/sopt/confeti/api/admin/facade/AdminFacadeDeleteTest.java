package org.sopt.confeti.api.admin.facade;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.music.artist.application.ArtistMusicAPIService;
import org.sopt.confeti.domain.music.artist.application.ArtistService;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftParser;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftService;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.ticketvendor.application.TicketVendorService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.event.S3FileDeleteEvent;
import org.sopt.confeti.global.transaction.Tx;
import org.sopt.confeti.global.transaction.TxRunner;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@ResourceLock("Tx.txRunner")
class AdminFacadeDeleteTest {

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
    void deleteConcert_연관_데이터를_함께_정리한다() {
        long concertId = 1L;
        long performanceId = 11L;
        Concert concert = org.mockito.Mockito.mock(Concert.class);

        given(concertService.findById(concertId)).willReturn(concert);
        given(concert.getPosterPath()).willReturn("poster.png");
        given(performanceService.deleteByTypeAndTypeId(PerformanceType.CONCERT, concertId))
            .willReturn(performanceId);

        adminFacade.deleteConcert(concertId);

        verify(concertFavoriteService).deleteAllByConcertId(concertId);
        verify(setlistService).deleteByTypeAndTypeId(SetlistType.CONCERT, concertId);
        verify(performanceService).deleteByTypeAndTypeId(PerformanceType.CONCERT, concertId);
        verify(concertService).delete(concertId);
        verify(concertService).deleteDetailCache(concertId);
        verify(performanceSearchService).deleteById(performanceId);

        ArgumentCaptor<S3FileDeleteEvent> captor = ArgumentCaptor.forClass(S3FileDeleteEvent.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().folderPath()).isEqualTo(
            FolderPath.combine(FolderPath.CONCERT, FolderPath.POSTER));
        assertThat(captor.getValue().filePath()).isEqualTo("poster.png");
    }

    @Test
    void deleteFestival_포스터와_로고까지_정리한다() {
        long festivalId = 2L;
        long performanceId = 22L;
        Festival festival = org.mockito.Mockito.mock(Festival.class);

        given(festivalService.findById(festivalId)).willReturn(festival);
        given(festival.getPosterPath()).willReturn("poster.png");
        given(festival.getLogoPath()).willReturn("logo.png");
        given(performanceService.deleteByTypeAndTypeId(PerformanceType.FESTIVAL, festivalId))
            .willReturn(performanceId);

        adminFacade.deleteFestival(festivalId);

        verify(setlistService).deleteByTypeAndTypeId(SetlistType.FESTIVAL, festivalId);
        verify(performanceService).deleteByTypeAndTypeId(PerformanceType.FESTIVAL, festivalId);
        verify(festivalService).delete(festivalId);
        verify(festivalService).deleteDetailCache(festivalId);
        verify(performanceSearchService).deleteById(performanceId);

        ArgumentCaptor<S3FileDeleteEvent> captor = ArgumentCaptor.forClass(S3FileDeleteEvent.class);
        verify(eventPublisher, org.mockito.Mockito.times(2)).publishEvent(captor.capture());
        assertThat(captor.getAllValues())
            .extracting(S3FileDeleteEvent::folderPath, S3FileDeleteEvent::filePath)
            .containsExactlyInAnyOrder(
                org.assertj.core.groups.Tuple.tuple(
                    FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                    "poster.png"
                ),
                org.assertj.core.groups.Tuple.tuple(
                    FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                    "logo.png"
                )
            );
    }
}
