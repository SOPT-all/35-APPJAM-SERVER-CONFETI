package org.sopt.confeti.domain.festivalstage;

import jakarta.persistence.*;

@Entity
@Table(name="festival_stages")
public class FestivalStage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festival_stage_id;

    @Column
    private Long festival_date_id;

    @Column(length = 30)
    private String name;

    @Column
    private Integer order;

    public Long getFestival_stage_id() {
        return festival_stage_id;
    }

    public Long getFestival_date_id() {
        return festival_date_id;
    }

    public String getName() {
        return name;
    }

    public Integer getOrder() {
        return order;
    }
}
