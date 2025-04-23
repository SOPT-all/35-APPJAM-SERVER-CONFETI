package org.sopt.confeti.domain.setlist.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.setlist.Setlist;
import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.domain.setlist.application.dto.request.SetlistMusicEditDto;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistMusicRepository;
import org.sopt.confeti.domain.setlist.infra.repository.SetlistRepository;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SetlistEditService {

    private final SetlistRepository setlistRepository;
    private final SetlistMusicRepository setlistMusicRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public void startEdit(Long userId, Long setlistId) {
        Setlist setlist = setlistRepository.findByIdAndUserId(setlistId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        List<SetlistMusic> musics = setlistMusicRepository.findBySetlist(setlist);
        List<SetlistMusicEditDto> musicDtos = musics.stream()
                .map(SetlistMusicEditDto::from)
                .toList();

        String redisKey = generateRedisKey(userId, setlistId);
        redisTemplate.opsForValue().set(redisKey, musicDtos);
    }

    private String generateRedisKey(Long userId, Long setlistId) {
        return "edit:setlist:" + userId + ":" + setlistId;
    }
}
