package org.sopt.confeti.global.converter;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.common.constant.TimetableSortType;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
public class TimetableSortTypeConverter implements Converter<String, TimetableSortType> {

    @Override
    public TimetableSortType convert(String source) {
        if (!StringUtils.hasText(source)) {
            return TimetableSortType.getDefault();
        }

        Optional<TimetableSortType> sortType = TimetableSortType.from(source);

        if (sortType.isEmpty()) {
            log.error(
                "TimetableSortTypeConverter.convert : Unknown timetable sort type. source : {}",
                source);
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }

        return sortType.get();
    }
}
