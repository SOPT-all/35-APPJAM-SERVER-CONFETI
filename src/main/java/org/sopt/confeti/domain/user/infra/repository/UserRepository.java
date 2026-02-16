package org.sopt.confeti.domain.user.infra.repository;

import java.util.Optional;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value =
        "SELECT DISTINCT u" +
            " FROM User u" +
            " LEFT JOIN FETCH u.timetables t" +
            " WHERE u.id = :userId"
    )
    Optional<User> findUserTimetablesById(final @Param("userId") long userId);

    Optional<User> findBySocialIdAndProvider(String socialId, OAuthProvider provider);

    boolean existsBySocialIdAndProvider(String socialId, OAuthProvider provider);

    @Query("SELECT u.hasTimetableHistory FROM User u WHERE u.id = :userId")
    boolean hasTimetableHistory(@Param("userId") long userId);
}
