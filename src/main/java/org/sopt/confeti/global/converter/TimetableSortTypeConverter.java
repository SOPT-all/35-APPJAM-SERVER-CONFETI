package org.sopt.confeti.global.converter;

import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.common.constant.TimetableSortType;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimetableSortTypeConverter implements Converter<String, TimetableSortType> {

    @Override
    public TimetableSortType convert(String source) {
        TimetableSortType sortType = TimetableSortType.from(source);

        if (sortType == TimetableSortType.UNKNOWN) {
            log.error("TimetableSortTypeConverter.convert : Unknown timetable sort type. source : {}", source);
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }

        return sortType;
    }
}
