package org.sopt.confeti.domain.setlist;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.user.User;

@Entity
@Table(name = "setlists")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Setlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SetlistType type;

    @Column(name = "type_id", nullable = false)
    private Long typeId;

    @OneToMany(mappedBy = "setlist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SetlistSong> songs = new ArrayList<>();

    @Builder
    public Setlist(User user, SetlistType type, Long typeId) {
        this.user = user;
        this.type = type;
        this.typeId = typeId;
    }

    public void addSongs(SetlistSong song) {
        songs.add(song);
        song.setSetlist(this);
    }
}
