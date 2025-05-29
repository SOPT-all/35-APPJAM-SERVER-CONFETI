package org.sopt.confeti.domain.setlist.application;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.domain.setlist.SetlistType;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistMusicEditDTO;
import org.sopt.confeti.api.setlist.facade.dto.request.SetlistUpdateMusicOrderDTO;
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

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(setlistEditService, "objectMapper", new ObjectMapper());
    }

    @Test
    void startEdit_편집_시작() {
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
        ArgumentCaptor<List<SetlistMusicEditDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(valueOperations).set(eq(expectedKey), captor.capture());

        List<SetlistMusicEditDTO> captured = captor.getValue();
        assertThat(captured).hasSize(1);
        assertThat(captured.get(0).trackId()).isEqualTo("203948575");
        assertThat(captured.get(0).trackName()).isEqualTo("Super Shy");
        assertThat(captured.get(0).orders()).isEqualTo(1);
    }

    @Test
    void updateMusicOrder_곡_순서_변경() {
        // given
        Long userId = 1L;
        Long setlistId = 100L;
        String redisKey = "edit:setlist:" + userId + ":" + setlistId;

        List<SetlistMusicEditDTO> originalList = List.of(
                new SetlistMusicEditDTO(1L, "track1", "Artist A", "Song A", "url1", "preview1", 1),
                new SetlistMusicEditDTO(2L, "track2", "Artist B", "Song B", "url2", "preview2", 2)
        );

        List<SetlistUpdateMusicOrderDTO> requestList = List.of(
                new SetlistUpdateMusicOrderDTO("track1", 1),
                new SetlistUpdateMusicOrderDTO("track2", 2)
        );

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(redisKey)).willReturn(originalList);

        // when
        setlistEditService.updateMusicOrder(userId, setlistId, requestList);

        // then
        ArgumentCaptor<List<SetlistMusicEditDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(valueOperations).set(eq(redisKey), captor.capture());

        List<SetlistMusicEditDTO> updated = captor.getValue();

        assertThat(updated).hasSize(2);
        assertThat(updated).anySatisfy(dto -> {
            if (dto.trackId().equals("track1")) {
                assertThat(dto.orders()).isEqualTo(2);
            } else if (dto.trackId().equals("track2")) {
                assertThat(dto.orders()).isEqualTo(1);
            }
        });
    }

    @Test
    void deleteMusic_곡_삭제_및_순서_재정렬() {
        // given
        Long userId = 1L;
        Long setlistId = 100L;
        String redisKey = "edit:setlist:" + userId + ":" + setlistId;

        List<SetlistMusicEditDTO> originalList = List.of(
                new SetlistMusicEditDTO(1L, "track1", "Artist A", "Song A", "url1", "preview1", 1),
                new SetlistMusicEditDTO(2L, "track2", "Artist B", "Song B", "url2", "preview2", 2),
                new SetlistMusicEditDTO(3L, "track3", "Artist C", "Song C", "url3", "preview3", 3)
        );

        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(redisKey)).willReturn(originalList);

        // when
        String deletedTrackId = setlistEditService.deleteMusic(userId, setlistId, 2);

        // then
        assertThat(deletedTrackId).isEqualTo("track2");

        ArgumentCaptor<List<SetlistMusicEditDTO>> captor = ArgumentCaptor.forClass(List.class);
        verify(valueOperations).set(eq(redisKey), captor.capture());

        List<SetlistMusicEditDTO> updated = captor.getValue();
        assertThat(updated).hasSize(2);
        assertThat(updated.get(0).orders()).isEqualTo(1);
        assertThat(updated.get(1).orders()).isEqualTo(2);
        assertThat(updated).extracting(SetlistMusicEditDTO::trackId).containsExactly("track1", "track3");
    }
}
