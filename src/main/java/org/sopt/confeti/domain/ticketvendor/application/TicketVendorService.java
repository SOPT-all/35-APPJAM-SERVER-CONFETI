package org.sopt.confeti.domain.ticketvendor.application;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;
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
}
