package org.sopt.confeti.domain.usertimetable;

import jakarta.persistence.*;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="user_timetables")
public class UserTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="festival_artist_id")
    private FestivalArtist festivalArtist;

    @Column(nullable = false)
    private boolean isSelected;

    public Long getId() {
        return id;
    }
    
    public User getUser() {
        return user;
    }

    public FestivalArtist getFestivalArtist() {
        return festivalArtist;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
