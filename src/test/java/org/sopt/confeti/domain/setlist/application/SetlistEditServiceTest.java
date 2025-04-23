package org.sopt.confeti.domain.setlist.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistMusicEditDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistMusicRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.constant.Role;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class SetlistEditServiceTest {

    @InjectMocks private SetlistEditService setlistEditService;
    @Mock private SetlistRepository setlistRepository;
    @Mock private SetlistMusicRepository setlistMusicRepository;
    @Mock private RedisTemplate<String, Object> redisTemplate;
    @Mock private ValueOperations<String, Object> valueOperations;

    @Test
    void startEdit_셋리스트_편집시작() {
        // given
        Long userId = 1L;
        Long setlistId = 100L;

        User user = User.builder()
                .provider(OAuthProvider.KAKAO)
                .socialId("kakao123")
                .name("정교")
                .profilePath(null)
                .role(Role.GENERAL)
                .build();

        Setlist setlist = Setlist.builder()
                .user(user)
                .type(SetlistType.CONCERT)
                .typeId(1L)
                .build();

        ReflectionTestUtils.setField(setlist, "id", setlistId);

        SetlistMusic music = SetlistMusic.builder()
                .trackId("203948575")
                .artistName("NewJeans")
                .trackName("Super Shy")
                .artworkUrl("https://img")
                .previewUrl("https://preview")
                .orders(1)
                .build();
        music.setSetlist(setlist);

        List<SetlistMusic> musics = List.of(music);

        given(setlistRepository.findByIdAndUserId(setlistId, userId)).willReturn(Optional.of(setlist));
        given(setlistMusicRepository.findBySetlist(setlist)).willReturn(musics);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        // when
        setlistEditService.startEdit(userId, setlistId);

        // then
        String expectedKey = "edit:setlist:" + userId + ":" + setlistId;
        ArgumentCaptor<List<SetlistMusicEditDto>> captor = ArgumentCaptor.forClass(List.class);
        verify(valueOperations).set(eq(expectedKey), captor.capture());

        List<SetlistMusicEditDto> captured = captor.getValue();
        assertThat(captured).hasSize(1);
        assertThat(captured.get(0).trackId()).isEqualTo("203948575");
        assertThat(captured.get(0).trackName()).isEqualTo("Super Shy");
        assertThat(captured.get(0).orders()).isEqualTo(1);
    }
}
