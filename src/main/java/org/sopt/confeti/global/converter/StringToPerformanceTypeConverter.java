package org.sopt.confeti.global.converter;

import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.ConfetiException;
import org.springframework.core.convert.converter.Converter;

public class StringToPerformanceTypeConverter implements Converter<String, PerformanceType> {

    @Override
    public PerformanceType convert(String source) {
        try {
            return PerformanceType.convert(source);
        } catch (ConfetiException | IllegalArgumentException e) {
            return null;
        }
    }
}
