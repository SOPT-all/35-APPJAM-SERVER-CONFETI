package org.sopt.confeti.global.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RedisProperty {
    private String host;
    private int port;
}
