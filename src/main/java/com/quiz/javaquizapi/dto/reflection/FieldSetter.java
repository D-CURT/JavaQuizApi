package com.quiz.javaquizapi.dto.reflection;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class FieldSetter<V> {
    private final Consumer<V> setter;
    private final FieldValue<V> applicable;
}
