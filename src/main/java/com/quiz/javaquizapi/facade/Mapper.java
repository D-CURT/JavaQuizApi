package com.quiz.javaquizapi.facade;

public interface Mapper {
    <D> D map(Object source, Class<D> destinationType);
}
