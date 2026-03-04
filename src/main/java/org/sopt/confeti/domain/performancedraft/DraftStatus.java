package org.sopt.confeti.domain.performancedraft;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DraftStatus {
    UNREVIEWED("검토 필요"),
    HOLD("보류"),
    ;

    @JsonValue
    private final String description;

}
