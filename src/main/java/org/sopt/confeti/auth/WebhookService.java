package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional
public class WebhookService {

    private final UserRepository userRepository;
    private final ApiRestClientBuilder restClient;

    @Value("${discord.webhook.url}")
    private String discordWebhookUrl;

    public void sendDiscordNotification() {

        long totalMembers = userRepository.count();

        String message = "CONFETI에 " + totalMembers + "번째 유저가 가입했습니다!🎉\n";

        Map<String, String> body = new HashMap<>();
        body.put("content", message);

        restClient.request()
                .post()
                .baseUrl(discordWebhookUrl)
                .body(body)
                .build()
                .connect()
                .retrieve();
    }
}
