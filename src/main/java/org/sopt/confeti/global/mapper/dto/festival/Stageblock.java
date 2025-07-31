package org.sopt.confeti.global.mapper.dto.festival;

import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Stageblock {
    private final String stageName;
    private final int order;
}
