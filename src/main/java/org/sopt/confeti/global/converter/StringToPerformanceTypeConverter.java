package org.sopt.confeti.global.converter;

import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.exception.ConfetiException;
import org.springframework.core.convert.converter.Converter;

public class StringToPerformanceTypeConverter implements Converter<String, PerformanceType_DEPRECATED> {

    @Override
    public PerformanceType_DEPRECATED convert(String source) {
        try {
            return PerformanceType_DEPRECATED.convert(source);
        } catch (ConfetiException | IllegalArgumentException e) {
            return null;
        }
    }
}
