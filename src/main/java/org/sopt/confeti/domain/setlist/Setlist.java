package org.sopt.confeti.domain.setlist;

import jakarta.persistence.*;
import java.util.*;
import lombok.*;
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
    private List<SetlistMusic> musics = new ArrayList<>();

    @Builder
    public Setlist(User user, SetlistType type, Long typeId) {
        this.user = user;
        this.type = type;
        this.typeId = typeId;
    }

    public void addMusics(SetlistMusic music) {
        musics.add(music);
        music.setSetlist(this);
    }
}
