package org.sopt.confeti.domain.ticketvendor.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorCreateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorCreateResponseDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.request.TicketVendorUpdateDto;
import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorUpdateResponseDto;
import org.sopt.confeti.domain.ticketvendor.infra.repository.TicketVendorRepository;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.exception.ConflictException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.sopt.confeti.domain.ticketvendor.application.dto.response.TicketVendorDtos;

@Service
@RequiredArgsConstructor
public class TicketVendorService {

    private final TicketVendorRepository ticketVendorRepository;

    @Transactional(readOnly = true)
    public TicketVendor getById(Long id) {
        return ticketVendorRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Transactional
    public TicketVendor getOrCreate(String name, String logoPath) {
        return ticketVendorRepository.findByName(name)
            .orElseGet(() -> ticketVendorRepository.save(
                TicketVendor.create(name, logoPath)
            ));
    }

    @Transactional
    public TicketVendorCreateResponseDto create(TicketVendorCreateDto dto) {
        if (ticketVendorRepository.findByName(dto.name()).isPresent()) {
            throw new ConflictException(ErrorMessage.CONFLICT);
        }
        TicketVendor savedVendor = ticketVendorRepository.save(dto.toEntity());
        return TicketVendorCreateResponseDto.of(savedVendor);
    }

    @Transactional
    public TicketVendorUpdateResponseDto update(TicketVendorUpdateDto dto) {
        TicketVendor ticketVendor = ticketVendorRepository.findById(dto.ticketVendorId())
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        if (dto.name() != null) {
            ticketVendorRepository.findByName(dto.name())
                .ifPresent(existingVendor -> {
                    if (!existingVendor.getId().equals(dto.ticketVendorId())) {
                        throw new ConflictException(ErrorMessage.CONFLICT);
                    }
                });
        }

        ticketVendor.update(dto.name(), dto.logoPath());
        return TicketVendorUpdateResponseDto.of(ticketVendor);
    }

    @Transactional
    public void delete(Long ticketVendorId) {
        TicketVendor ticketVendor = ticketVendorRepository.findById(ticketVendorId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        
        ticketVendorRepository.delete(ticketVendor);
    }

    @ReadOnlyTransactional
    public TicketVendorDtos findAll() {
        return TicketVendorDtos.from(
            ticketVendorRepository.findAll().stream()
                .map(TicketVendorDto::of)
                .toList()
        );
    }
}
