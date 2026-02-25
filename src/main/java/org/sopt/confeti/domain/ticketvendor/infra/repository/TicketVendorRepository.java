package org.sopt.confeti.domain.ticketvendor.infra.repository;

import java.util.Optional;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketVendorRepository extends JpaRepository<TicketVendor, Long> {
    Optional<TicketVendor> findByName(String name);
}
