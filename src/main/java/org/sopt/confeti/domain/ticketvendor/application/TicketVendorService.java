package org.sopt.confeti.domain.ticketvendor.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorUpdateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.TicketVendorUpdateResponseDto;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TicketVendorService {

    private final TicketVendorRepository ticketVendorRepository;

    @Transactional
    public TicketVendor getOrCreate(String name, String logoPath) {
        return ticketVendorRepository.findByName(name)
            .orElseGet(() -> ticketVendorRepository.save(
                TicketVendor.builder()
                    .name(name)
                    .logoPath(logoPath)
                    .build()
            ));
    }

    @Transactional
    public TicketVendorCreateResponseDto create(TicketVendorCreateDto dto) {
        if (ticketVendorRepository.findByName(dto.name()).isPresent()) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
        TicketVendor savedVendor = ticketVendorRepository.save(
            TicketVendor.builder()
                .name(dto.name())
                .logoPath(dto.logoPath())
                .build()
        );
        return TicketVendorCreateResponseDto.of(savedVendor);
    }

    @Transactional
    public TicketVendorUpdateResponseDto update(TicketVendorUpdateDto dto) {
        TicketVendor ticketVendor = ticketVendorRepository.findById(dto.ticketVendorId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        ticketVendorRepository.findByName(dto.name())
            .ifPresent(existingVendor -> {
                if (!existingVendor.getId().equals(dto.ticketVendorId())) {
                    throw new ConflictException(ErrorMessage.CONFLICT);
                }
            });

        ticketVendor.update(dto.name(), dto.logoPath());
        return TicketVendorUpdateResponseDto.of(ticketVendor);
    }

    @Transactional
    public void delete(Long ticketVendorId) {
        TicketVendor ticketVendor = ticketVendorRepository.findById(ticketVendorId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        
        ticketVendorRepository.delete(ticketVendor);
    }
}
