package org.sopt.confeti.domain.user.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;

@AllArgsConstructor
public enum Role {
    ONBOARDING("onboarding"),
    GENERAL("general"),
    ADMIN("admin");

    private final String type;

    @JsonCreator
    public static Role from(final String input) {
        return Arrays.stream(Role.values())
                .filter(role -> role.type.equals(input.toLowerCase()))
                .findFirst()
                .orElseThrow(
                        () -> new ConfetiException(ErrorMessage.BAD_REQUEST)
                );
    }
}
