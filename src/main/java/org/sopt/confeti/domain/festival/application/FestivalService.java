package org.sopt.confeti.domain.festival.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.CreateFestivalDTO;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.infra.repository.FestivalRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.artistsearcher.ArtistResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepository festivalRepository;
    private  final ArtistResolver artistResolver;

    @Transactional(readOnly = true)
    public Festival findById(Long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        return festival;
    }

    @Transactional(readOnly = true)
    public Festival getFestivalDetailByFestivalId(final long festivalId) {
        Festival festival = festivalRepository.findById(festivalId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        artistResolver.load(festival);

        return festival;
    }

    @Transactional
    public void create(final CreateFestivalDTO createFestivalDTO) {
        festivalRepository.save(
                Festival.create(createFestivalDTO)
        );
    }

    @Transactional(readOnly = true)
    public boolean existsById(final long festivalId) {
        return festivalRepository.existsById(festivalId);
    }

    @Transactional
    public List<Festival> findFestivalsByIdIn(final List<Long> festivalIds) {
        return festivalRepository.findFestivalsByIdIn(festivalIds);
    }
}
