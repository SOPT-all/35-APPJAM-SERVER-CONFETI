package org.sopt.confeti.global.converter;

import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.exception.BadRequestException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PerformanceStatusConverter implements Converter<String, PerformanceStatus> {

    @Override
    public PerformanceStatus convert(String source) {
        PerformanceStatus performanceStatus = PerformanceStatus.from(source);

        if (performanceStatus == PerformanceStatus.UNKNOWN) {
            log.error("PerformanceStatusConverter.convert : Unknown performance status. source : {}", source);
            throw new BadRequestException(ErrorMessage.BAD_REQUEST);
        }

        return performanceStatus;
    }
}
