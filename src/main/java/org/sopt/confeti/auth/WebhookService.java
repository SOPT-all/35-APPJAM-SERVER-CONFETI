package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WebhookService {

    private final UserRepository userRepository;
    private final ApiRestClientBuilder restClient;
    private final Environment environment;

    public void sendDiscordNotification() {

        for (String profile : environment.getActiveProfiles()) {
            if (profile.equalsIgnoreCase("dev")) return;
        }

        String discordWebhookUrl = environment.getProperty("discord.webhook.url");
        if (discordWebhookUrl == null || discordWebhookUrl.isBlank()) return;

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
