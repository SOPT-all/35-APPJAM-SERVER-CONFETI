package org.sopt.confeti.domain.ticketvendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "ticket_vendors")
public class TicketVendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String logoPath;

    @Builder
    public TicketVendor(String name, String logoPath) {
        this.name = name;
        this.logoPath = logoPath;
    }
}
