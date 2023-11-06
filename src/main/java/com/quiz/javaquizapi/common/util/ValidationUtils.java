package com.quiz.javaquizapi.common.util;

import com.quiz.javaquizapi.exception.ValidationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/**
 * Provides validation operations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtils {
    /**
     * Provides a validator with no error messages.
     * @return a validator {@link Validator}.
     */
    public static Validator validator() {
        return new Validator(null);
    }

    /**
     * Provides a validator with an error message.
     * @param error the error message.
     * @return a validator {@link  Validator}.
     */
    public static Validator validator(String error) {
        return new Validator(error);
    }

    @RequiredArgsConstructor
    public static class Validator {
        private final String error;

        /**
         * Validates the entity code using UUID utility class.
         * @param code the entity code.
         * @return a valid code.
         * @throws ValidationException if the provided code doesn't match the UUID format.
         */
        public String validateCode(String code) {
            try {
                UUID.fromString(code);
            } catch (Exception e) {
                throw ValidationException.getCodeMalformedException(error, code);
            }
            return code;
        }
    }
}
