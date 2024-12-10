package com.rifftech.temporal.collections;

import lombok.NonNull;

public class SystemTemporalCollection<T> extends AbstractTemporalCollection<T, SystemTemporalValue<T>> {
    @Override
    protected SystemTemporalValue<T> temporalValue(@NonNull TemporalRange validTemporalRange, @NonNull T value) {
        return new SystemTemporalValue<>(validTemporalRange, value);
    }
}
