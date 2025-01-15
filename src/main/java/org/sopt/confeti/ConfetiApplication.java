package org.sopt.confeti;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.util.artistsearcher.SpotifyAPIHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@RequiredArgsConstructor
public class ConfetiApplication {

    private final ApplicationContext ac;

    public static void main(String[] args) {
        SpringApplication.run(ConfetiApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        SpotifyAPIHandler spotifyAPIHandler = ac.getBean(SpotifyAPIHandler.class);
        spotifyAPIHandler.init();
    }
}
