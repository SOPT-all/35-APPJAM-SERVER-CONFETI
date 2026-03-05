package org.sopt.confeti.global.transaction;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TxConfig {

    private final TxRunner txRunner;

    @PostConstruct
    public void init() {
        Tx.initialize(txRunner);
    }
}
