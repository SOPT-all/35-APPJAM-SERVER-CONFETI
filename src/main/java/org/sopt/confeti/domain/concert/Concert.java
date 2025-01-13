package org.sopt.confeti.domain.concert;

import jakarta.persistence.*;

import java.security.Timestamp;

@Entity
@Table(name="concerts")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concert_id;

    @Column
    private String concert_title;

    @Column
    private Timestamp concert_start_at;

    @Column
    private Timestamp concert_end_at;

    @Column
    private String concert_area;

    @Column
    private String concert_poster_path;

    @Column
    private Timestamp reserve_at;

    @Column
    private String reservation_url;

    @Column
    private String reservation_office;

    @Column
    private String age_rating;

    @Column
    private String time;

    @Column
    private String price;

    @Column
    private String info_img_url;

    public Long getConcert_id() {
        return concert_id;
    }

    public String getConcert_title() {
        return concert_title;
    }

    public String getAge_rating() {
        return age_rating;
    }

    public String getConcert_area() {
        return concert_area;
    }

    public String getConcert_poster_path() {
        return concert_poster_path;
    }

    public String getInfo_img_url() {
        return info_img_url;
    }

    public String getPrice() {
        return price;
    }

    public String getReservation_office() {
        return reservation_office;
    }

    public String getReservation_url() {
        return reservation_url;
    }

    public String getTime() {
        return time;
    }

    public Timestamp getConcert_end_at() {
        return concert_end_at;
    }

    public Timestamp getConcert_start_at() {
        return concert_start_at;
    }

    public Timestamp getReserve_at() {
        return reserve_at;
    }
}
