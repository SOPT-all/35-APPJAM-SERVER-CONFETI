package org.sopt.confeti.global.notification;

import org.sopt.confeti.global.module.rest_client.builder.ApiRestClientBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Profile(
        value = "prod"
)
public class SlackNotificationAgent implements NotificationAgent {

    private final Map<SlackNotificationType, SlackNotificationUrl> notificationUrlMap = new HashMap<>();
    private final ApiRestClientBuilder restClient;

    public SlackNotificationAgent(
            List<SlackNotificationUrl> slackNotificationUrls,
            ApiRestClientBuilder restClient
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

        String slackMessage = makeSlackMessage(message);
        restClient.request()
                .post()
                .baseUrl(notificationUrl.getUrl())
                .body(new SlackMessage(slackMessage))
                .build()
                .connect()
                .retrieve();
    }

    private String makeSlackMessage(String message) {
        List<String> addedPrefixMessage = Arrays.stream(message.split("\n"))
                .map(line -> "> " + line)
                .toList();

        return String.format(
                "> 📛5XX 에러\n" + "> Environment: Prod\n%s",
                String.join("\n", addedPrefixMessage)
        );
    }
}
