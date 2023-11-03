package com.quiz.javaquizapi.dto.reflection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class FieldValue<V> {
    private final V value;
}
