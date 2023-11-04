package com.quiz.javaquizapi.common.util;

import com.quiz.javaquizapi.dto.BaseDto;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * Provides utility methods to operate with text.
 */
public class TextUtils {
    public static <D extends BaseDto> String beautifyDtoName(Class<D> type) {
        var entityName = StringUtils.removeEnd(type.getSimpleName(), "Dto");
        return capitalize(
                join(StringUtils.splitByCharacterTypeCamelCase(entityName), StringUtils.SPACE)
        );
    }
}
