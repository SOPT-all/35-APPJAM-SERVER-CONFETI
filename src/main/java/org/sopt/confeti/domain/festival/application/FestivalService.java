package org.sopt.confeti.domain.festival.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalDetailInfo;
import org.sopt.confeti.api.admin.facade.dto.response.AdminFestivalPreviewInfo;
import org.sopt.confeti.api.dummy.facade.dto.festival.request.CreateFestivalDateDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.dto.FestivalCursorDTO;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.domain.festival_date.FestivalDate;
import org.sopt.confeti.domain.festival_date.application.FestivalDateService;
import org.sopt.confeti.domain.festival_stage.FestivalStage;
import org.sopt.confeti.domain.festival_stage.application.FestivalStageService;
import org.sopt.confeti.domain.festival_time.application.FestivalTimeService;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FestivalService {

    private static final String START_AT_COLUMN = "startAt";
    private static final int INIT_PAGE = 0;
    private static final String TITLE_COLUMN = "title";

    private final FestivalRepository festivalRepository;
    private final FestivalDateService festivalDateService;
    private final FestivalStageService festivalStageService;
    private final FestivalTimeService festivalTimeService;
    private final RedisHandler redisHandler;

    @ReadOnlyTransactional
    public Festival findById(Long festivalId) {
        return festivalRepository.findById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    // TODO: AOP 방식으로 캐싱 전략 수정
    @Transactional(readOnly = true)
    public FestivalDetailDTO getUpcomingFestivalDetailByFestivalId(long festivalId) {
        Optional<FestivalDetailDTO> cachedFestival = redisHandler.get(
            RedisKey.PERFORMANCE_FESTIVALS.createKeyInfo(festivalId));
        if (cachedFestival.isPresent()) {
            return cachedFestival.get();
        }

        Festival festival = festivalRepository.findUpcomingWithDatesById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        festivalDateService.findDatesWithArtistsByFestivalId(festivalId);
        festivalRepository.findUpcomingWithReservationUrlsById(festivalId);

        FestivalDetailDTO festivalDetail = FestivalDetailDTO.from(festival);
        redisHandler.set(RedisKey.PERFORMANCE_FESTIVALS.createKeyInfo(festivalId), festivalDetail);
        return festivalDetail;
    }

    @ReadOnlyTransactional
    public boolean existsById(final long festivalId) {
        return festivalRepository.existsById(festivalId);
    }

    @Transactional
    public List<Festival> findFestivalsByIdIn(final List<Long> festivalIds) {
        return festivalRepository.findFestivalsByIdIn(festivalIds);
    }

    @ReadOnlyTransactional
    public List<Festival> findSupportedTimetableFestivalsUsingInitCursor(final long userId,
        final int size) {
        return festivalRepository.findFestivalsUsingInitCursorAndSupportStatus(
            userId,
            getPageRequestWithSort(size, getFestivalSort()),
            TimetableSupportStatus.SUPPORTED
        );
    }

    private PageRequest getPageRequestWithSort(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getFestivalSort() {
        return Sort.by(
            Order.asc(TITLE_COLUMN));
    }

    @ReadOnlyTransactional
    public List<Festival> findSupportedTimetableFestivalsUsingCursor(
        final long userId,
        final String cursorTitle,
        final boolean cursorIsFavorite,
        final int size
    ) {
        return festivalRepository.findFestivalsUsingCursorAndSupportStatus(
            userId,
            cursorTitle,
            cursorIsFavorite,
            getPageRequestWithSort(size, getFestivalSort()),
            TimetableSupportStatus.SUPPORTED
        );
    }

    @ReadOnlyTransactional
    public Optional<FestivalCursorDTO> findFestivalCursor(final long userId,
        final long festivalId) {
        return festivalRepository.findFestivalCursor(userId, festivalId);
    }

    @Transactional
    public long create(Festival festival) {
        return festivalRepository.save(festival).getId();
    }

    @Transactional
    public void addDates(long festivalId, List<CreateFestivalDateDTO> dates) {
        Festival festival = findById(festivalId);

        festival.addDates(
            dates.stream()
                .map(FestivalDate::create)
                .toList());
    }

    @Transactional(readOnly = true)
    public AdminFestivalDetailInfo getAdminFestivalDetailInfo(long festivalId) {
        Festival festival = festivalRepository.findWithDatesById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        festivalDateService.findDatesWithArtistsByFestivalId(festivalId);

        festivalDateService.findDatesWithStagesByFestivalId(festivalId);

        List<Long> dateIds = festival.getDates().stream()
            .map(FestivalDate::getId)
            .toList();
        festivalStageService.findStagesWithTimesByDateIds(dateIds);

        List<Long> stageIds = festival.getDates().stream()
            .flatMap(date -> date.getStages().stream())
            .map(FestivalStage::getId)
            .toList();
        if (!stageIds.isEmpty()) {
            festivalTimeService.findTimesWithArtistsByStageIds(stageIds);
        }

        return AdminFestivalDetailInfo.from(festival);
    }

    @Transactional
    public Festival getWithRelationsById(long festivalId) {
        Festival festival = festivalRepository.findWithDatesById(festivalId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        festivalDateService.findDatesWithArtistsByFestivalId(festivalId);
        festivalDateService.findDatesWithStagesByFestivalId(festivalId);

        List<Long> dateIds = festival.getDates().stream()
            .map(FestivalDate::getId)
            .toList();
        festivalStageService.findStagesWithTimesByDateIds(dateIds);

        List<Long> stageIds = festival.getDates().stream()
            .flatMap(date -> date.getStages().stream())
            .map(FestivalStage::getId)
            .toList();
        if (!stageIds.isEmpty()) {
            festivalTimeService.findTimesWithArtistsByStageIds(stageIds);
        }

        festivalRepository.findUpcomingWithReservationUrlsById(festivalId);

        return festival;
    }

    @ReadOnlyTransactional
    public List<Festival> getAll() {
        return festivalRepository.findAll();
    }

    @ReadOnlyTransactional
    public List<AdminFestivalPreviewInfo> getAdminFestivals() {
        return festivalRepository.findAll().stream()
            .map(AdminFestivalPreviewInfo::from)
            .toList();
    }
}
