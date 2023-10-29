package com.quiz.javaquizapi.dto;

/**
 * Provides a functionality to nullify a dto fields.
 * @implNote it's used while conversion operations.
 */
public interface Nullifiable {
    Nullifiable nullify();
}
