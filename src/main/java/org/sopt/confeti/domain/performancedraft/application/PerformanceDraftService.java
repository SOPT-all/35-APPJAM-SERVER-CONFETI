package org.sopt.confeti.domain.performancedraft.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftCreateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.request.PerformanceDraftUpdateDto;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;
import org.sopt.confeti.domain.performancedraft.infra.PerformanceDraftRepository;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.util.S3FileHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerformanceDraftService {

    private final PerformanceDraftRepository draftRepository;
    private final S3FileHandler s3FileHandler;

    @Transactional
    public PerformanceDraftDto createDraft(PerformanceDraftCreateDto dto, String posterPath, String logoPath) {
        PerformanceDraft draft = dto.toEntity(posterPath, logoPath);
        PerformanceDraft savedDraft = draftRepository.save(draft);
        return PerformanceDraftDto.from(savedDraft);
    }


}
