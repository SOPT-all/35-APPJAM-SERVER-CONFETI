package org.sopt.confeti.domain.festival.infra;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 현재는 타임테이블 생성이 불가능한 상태와 가능한 상태만 존재하지만, 
 * 확장성을 고려해 boolean이 아닌 Enum으로 관리함
 */
@Getter
@AllArgsConstructor
public enum TimetableSupportStatus {
    NONE("타임테이블 생성 불가"), 
    SUPPORTED("타임테이블 생성 가능"),
    ;

    private final String description;
}
