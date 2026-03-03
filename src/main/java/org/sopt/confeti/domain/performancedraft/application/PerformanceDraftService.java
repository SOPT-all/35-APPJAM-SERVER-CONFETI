package org.sopt.confeti.domain.performancedraft.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftCreateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftUpdateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDtos;
import org.sopt.confeti.domain.performancedraft.infra.PerformanceDraftRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PerformanceDraftService {

    private final PerformanceDraftRepository draftRepository;

    @Transactional
    public PerformanceDraftDto createDraft(PerformanceDraftCreateDto dto, String posterPath, String logoPath) {
        PerformanceDraft draft = dto.toEntity(posterPath, logoPath);
        PerformanceDraft savedDraft = draftRepository.save(draft);
        return PerformanceDraftDto.from(savedDraft);
    }

    @Transactional
    public PerformanceDraftDto updateDraft(PerformanceDraftUpdateDto dto, String posterPath, String logoPath) {
        PerformanceDraft draft = getById(dto.id());
        draft.update(dto.performanceType(), dto.status(), dto.performanceData(), posterPath, logoPath);
        return PerformanceDraftDto.from(draft);
    }

    @Transactional(readOnly = true)
    public PerformanceDraftDtos getAllDrafts() {
        return PerformanceDraftDtos.from(
                draftRepository.findAll().stream()
                        .map(PerformanceDraftDto::from)
                        .toList()
        );
    }

    @Transactional
    public void deleteDraft(Long draftId) {
        PerformanceDraft draft = getById(draftId);
        draftRepository.delete(draft);
    }

    @Transactional(readOnly = true)
    public PerformanceDraft getById(Long id) {
        return draftRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }
}
