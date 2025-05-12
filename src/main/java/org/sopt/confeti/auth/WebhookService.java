package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class WebhookService {

    private final UserRepository userRepository;

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    public void sendDiscordNotification() {

        RestTemplate restTemplate = new RestTemplate();

        long totalMembers = userRepository.count();

        String message = "CONFETI에 " + totalMembers + "번째 유저가 가입했습니다!🎉\n";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("content", message);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.postForEntity(discordWebhookUrl, requestEntity, String.class);
    }
}
