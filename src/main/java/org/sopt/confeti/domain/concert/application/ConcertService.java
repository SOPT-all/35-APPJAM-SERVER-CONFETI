package org.sopt.confeti.domain.concert.application;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.infra.repository.ConcertRepository;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final RedisHandler redisHandler;
    private final ConcertRepository concertRepository;

    // TODO: AOP 방식으로 캐싱 전략 수정
    @Transactional(readOnly = true)
    public ConcertDetailDTO getExpectedConcertDetailByConcertId(long concertId) {
        Optional<ConcertDetailDTO> cachedConcert = redisHandler.get(
            RedisKey.PERFORMANCE_CONCERTS.createKeyInfo(concertId));
        if (cachedConcert.isPresent()) {
            return cachedConcert.get();
        }

        Concert concert = concertRepository.findExpectedWithArtistsById(concertId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        concertRepository.findExpectedWithReservationUrlsById(concertId);

        ConcertDetailDTO concertDetail = ConcertDetailDTO.from(concert);
        redisHandler.set(RedisKey.PERFORMANCE_CONCERTS.createKeyInfo(concertId), concertDetail);
        return concertDetail;
    }

    @Transactional(readOnly = true)
    public boolean existsById(long concertId) {
        return concertRepository.existsById(concertId);
    }

    @Transactional(readOnly = true)
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
}
