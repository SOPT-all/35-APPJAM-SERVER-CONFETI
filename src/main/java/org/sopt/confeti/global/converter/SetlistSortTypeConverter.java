package org.sopt.confeti.global.converter;

import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.domain.setlist.SetlistSortType;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SetlistSortTypeConverter implements Converter<String, SetlistSortType> {

    @Override
    public SetlistSortType convert(String source) {
        SetlistSortType sortType = SetlistSortType.from(source);

        if (sortType == SetlistSortType.UNKNOWN) {
            log.error(
                "SetlistSortTypeConverter.convert : Unknown setlist sort type. source : {}",
                source);
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }

        return sortType;
    }
}
