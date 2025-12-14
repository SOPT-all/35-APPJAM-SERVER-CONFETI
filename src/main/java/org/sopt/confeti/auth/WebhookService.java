package org.sopt.confeti.auth;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class WebhookService {

    private final UserRepository userRepository;
    private final ApiRestClientBuilder restClient;
    private final Environment environment;

    public void sendDiscordNotification() {

        boolean isProd = Arrays.stream(environment.getActiveProfiles())
                .anyMatch(profile -> profile.equalsIgnoreCase("prod"));

        if (!isProd) {
            return;
        }

        String discordWebhookUrl = environment.getProperty("notification.discord.webhook.url");
        if (discordWebhookUrl == null || discordWebhookUrl.isBlank()) {
            return;
        }

        long totalMembers = userRepository.count();
        log.debug("Total Members : {}", totalMembers);

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
