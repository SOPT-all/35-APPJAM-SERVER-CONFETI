package org.sopt.confeti.domain.performancedraft.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.admin.facade.dto.response.AdminPerformanceDraftPreviewInfo;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftFileInfo;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftCreateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftUpdateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftInfo;
import org.sopt.confeti.domain.performancedraft.infra.PerformanceDraftRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class PerformanceDraftService {

    private final PerformanceDraftRepository draftRepository;
    private final PerformanceDraftParser draftParser;
    private final PerformanceDraftFileService performanceDraftFileService;

    @Transactional
    public PerformanceDraftInfo createDraft(PerformanceDraftCreateDto dto, String posterPath, String logoPath) {
        PerformanceDraft draft = dto.toEntity(posterPath, logoPath);
        PerformanceDraft savedDraft = draftRepository.save(draft);
        PerformanceDraftDto draftDto = PerformanceDraftDto.from(savedDraft);
        return PerformanceDraftInfo.of(draftDto, performanceDraftFileService.getFileInfo(draftDto));
    }

    @Transactional
    public PerformanceDraftInfo updateDraft(PerformanceDraftUpdateDto dto, String posterPath, String logoPath) {
        PerformanceDraft draft = getById(dto.id());
        draft.update(dto.performanceType(), dto.status(), dto.performanceData(), posterPath, logoPath);
        PerformanceDraftDto draftDto = PerformanceDraftDto.from(draft);
        return PerformanceDraftInfo.of(draftDto, performanceDraftFileService.getFileInfo(draftDto));
    }

    @ReadOnlyTransactional
    public List<AdminPerformanceDraftPreviewInfo> getAdminPerformanceDraftPreviews(String keyword) {
        return findDraftsByKeyword(keyword).stream()
            .map(draft -> {
                PerformanceDraftDto dto = PerformanceDraftDto.from(draft);
                PerformanceDraftFileInfo fileInfo = performanceDraftFileService.getFileInfo(dto);
                return AdminPerformanceDraftPreviewInfo.of(dto, draftParser, fileInfo);
            })
            .toList();
    }

    private List<PerformanceDraft> findDraftsByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return draftRepository.findAll();
        }
        return draftRepository.searchByKeyword(keyword);
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

    @Transactional(readOnly = true)
    public PerformanceDraftInfo getDraftById(Long id) {
        PerformanceDraftDto dto = PerformanceDraftDto.from(getById(id));
        return PerformanceDraftInfo.of(dto, performanceDraftFileService.getFileInfo(dto));
    }
}
