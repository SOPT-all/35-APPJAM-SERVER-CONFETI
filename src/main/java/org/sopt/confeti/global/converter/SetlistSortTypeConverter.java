package org.sopt.confeti.global.converter;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class SetlistSortTypeConverter implements Converter<String, SetlistSortType> {

    @Override
    public SetlistSortType convert(String source) {
        if (!StringUtils.hasText(source)) {
            return SetlistSortType.getDefault();
        }

        Optional<SetlistSortType> sortType = SetlistSortType.from(source);

        if (sortType.isEmpty()) {
            log.error(
                "SetlistSortTypeConverter.convert : Unknown setlist sort type. source : {}",
                source);
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }

        return sortType.get();
    }
}
