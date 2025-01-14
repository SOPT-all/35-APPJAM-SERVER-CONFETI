package org.sopt.confeti.domain.usertimetable;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name="user_timetables")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTimetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_timetable_id")
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

    @Builder
    public UserTimetable(User user, FestivalArtist festivalArtist, boolean isSelected) {
        this.user = user;
        this.festivalArtist = festivalArtist;
        this.isSelected = isSelected;
    }

    public static UserTimetable create(User user, FestivalArtist festivalArtist, boolean isSelected) {
        return UserTimetable.builder()
                .user(user)
                .festivalArtist(festivalArtist)
                .isSelected(isSelected)
                .build();
    }
}
