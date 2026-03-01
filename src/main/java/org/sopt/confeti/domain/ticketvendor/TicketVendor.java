package org.sopt.confeti.domain.ticketvendor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.sopt.confeti.domain.concert_reservation_url.ConcertReservationUrl;
import org.sopt.confeti.domain.festival_reservation_url.FestivalReservationUrl;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "ticket_vendors")
public class TicketVendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 250, nullable = false)
    private String logoPath;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticketVendor", cascade = CascadeType.REMOVE)
    private List<FestivalReservationUrl> festivalReservationUrls = new ArrayList<>();
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ticketVendor", cascade = CascadeType.REMOVE)
    private List<ConcertReservationUrl> concertReservationUrls = new ArrayList<>();
    
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public TicketVendor(String name, String logoPath) {
        this.name = name;
        this.logoPath = logoPath;
    }

    public static TicketVendor create(String name, String logoPath) {
        return TicketVendor.builder()
            .name(name)
            .logoPath(logoPath)
            .build();
    }

    public void update(String name, String logoPath) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (logoPath != null && !logoPath.isBlank()) {
            this.logoPath = logoPath;
        }
    }
}
