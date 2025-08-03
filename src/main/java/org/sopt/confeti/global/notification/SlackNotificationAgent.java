package org.sopt.confeti.global.notification;

import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Profile(
        value = { "prod", "dev" }
)
public class SlackNotificationAgent implements NotificationAgent {

    private final Map<SlackNotificationType, SlackNotificationUrl> notificationUrlMap = new HashMap<>();
    private final ApiRestClientBuilder restClient;

    public SlackNotificationAgent(
            @Autowired List<SlackNotificationUrl> slackNotificationUrls,
            @Autowired ApiRestClientBuilder restClient
    ) {
        this.restClient = restClient;

        slackNotificationUrls.forEach(notificationUrl -> {
            notificationUrlMap.put(notificationUrl.getType(), notificationUrl);
        });
    }

    @Override
    public void notify(NotificationType type, String message) {
        SlackNotificationType notificationType = (SlackNotificationType) type;
        SlackNotificationUrl notificationUrl = notificationUrlMap.get(notificationType);

        String slackMessage = makeSlackMessage(notificationType, message);
        restClient.request()
                .post()
                .baseUrl(notificationUrl.getUrl())
                .body(new SlackMessage(slackMessage))
                .build()
                .connect()
                .retrieve(String.class);
    }

    private String makeSlackMessage(SlackNotificationType type, String message) {
        List<String> addedPrefixMessage = Arrays.stream(message.split("\n"))
                .map(line -> "> " + line)
                .toList();

        return String.format(
                "> %s\n" + "> Environment: Prod\n%s",
                type.getTitle(),
                String.join("\n", addedPrefixMessage)
        );
    }
}
