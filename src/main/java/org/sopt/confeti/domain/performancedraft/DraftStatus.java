package org.sopt.confeti.domain.performancedraft;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DraftStatus {
    UNREVIEWED("검토 필요"),
    HOLD("보류"),
    ;

    private final String description;

}
