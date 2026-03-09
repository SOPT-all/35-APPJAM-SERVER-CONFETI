package org.sopt.confeti.domain.concert.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.facade.dto.response.AdminConcertDetailInfo;
import org.sopt.confeti.domain.concert.application.dto.ConcertPreviewInfo;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final RedisHandler redisHandler;
    private final ConcertRepository concertRepository;

    // TODO: AOP 방식으로 캐싱 전략 수정
    @Transactional(readOnly = true)
    public ConcertDetailDTO getUpcomingConcertDetailByConcertId(long concertId) {
        Optional<ConcertDetailDTO> cachedConcert = redisHandler.get(
            RedisKey.PERFORMANCE_CONCERTS.createKeyInfo(concertId));
        if (cachedConcert.isPresent()) {
            return cachedConcert.get();
        }

        Concert concert = concertRepository.findUpcomingWithArtistsById(concertId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        concertRepository.findUpcomingWithReservationUrlsById(concertId);

        ConcertDetailDTO concertDetail = ConcertDetailDTO.from(concert);
        redisHandler.set(RedisKey.PERFORMANCE_CONCERTS.createKeyInfo(concertId), concertDetail);
        return concertDetail;
    }

    @Transactional(readOnly = true)
    public AdminConcertDetailInfo getAdminConcertDetailInfo(long concertId) {
        Concert concert = concertRepository.findWithArtistsById(concertId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        concertRepository.findWithReservationUrlsById(concertId);
        return AdminConcertDetailInfo.from(concert);
    }

    @ReadOnlyTransactional
    public List<ConcertPreviewInfo> getAllConcerts(String keyword) {
        return findConcertsByKeyword(keyword).stream()
            .map(ConcertPreviewInfo::from)
            .toList();
    }

    private List<Concert> findConcertsByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return concertRepository.findAll();
        }
        return concertRepository.findAllByTitleContainingOrAreaContaining(keyword, keyword);
    }

    @ReadOnlyTransactional
    public boolean existsById(long concertId) {
        return concertRepository.existsById(concertId);
    }

    @ReadOnlyTransactional
    public Concert findById(final long concertId) {
        return concertRepository.findById(concertId)
            .orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
            );
    }

    @Transactional
    public long create(Concert concert) {
        return concertRepository.save(concert).getId();
    }

    @Transactional
    public Concert findWithRelationsById(long concertId) {
        Concert concert = concertRepository.findWithArtistsById(concertId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        concertRepository.findWithReservationUrlsById(concertId);
        return concert;
    }
}
