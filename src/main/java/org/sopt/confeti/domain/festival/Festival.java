package org.sopt.confeti.domain.festival;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name="festivals")
public class Festival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long festival_id;

    @Column(length = 50)
    private String festival_title;

    @Column
    private Timestamp festival_start_at;

    @Column
    private Timestamp festival_end_at;

    @Column(length = 30)
    private String festival_area;

    @Column(length = 250)
    private String festival_poster_path;

    @Column
    private Timestamp reserve_at;

    @Column(length = 250)
    private String reservation_url;

    @Column(length = 30)
    private String reservation_office;

    @Column(length = 30)
    private String age_rating;

    @Column(length = 30)
    private String time;

    @Column(length = 100)
    private String price;

    @Column(length = 250)
    private String info_img_url;

    public Long getConcert_id() {
        return festival_id;
    }

    public String getFestival_title() {
        return festival_title;
    }

    public Timestamp getFestival_start_at() {
        return festival_start_at;
    }

    public Timestamp getFestival_end_at() {
        return festival_end_at;
    }

    public String getFestival_area() {
        return festival_area;
    }

    public String getFestival_poster_path() {
        return festival_poster_path;
    }

    public Timestamp getReserve_at() {
        return reserve_at;
    }

    public String getReservation_url() {
        return reservation_url;
    }

    public String getReservation_office() {
        return reservation_office;
    }

    public String getAge_rating() {
        return age_rating;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getInfo_img_url() {
        return info_img_url;
    }
}

