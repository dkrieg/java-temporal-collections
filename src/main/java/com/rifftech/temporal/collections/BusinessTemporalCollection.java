package com.rifftech.temporal.collections;

import lombok.NonNull;

public class BusinessTemporalCollection<T> extends AbstractTemporalCollection<T, BusinessTemporalValue<T>> {
    @Override
    protected BusinessTemporalValue<T> temporalValue(@NonNull TemporalRange validTemporalRange, @NonNull T value) {
        return new BusinessTemporalValue<>(validTemporalRange, value);
    }
}
