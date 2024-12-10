package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;

public interface MutableTemporalCollection<T, V extends TemporalValue<T>> extends TemporalCollection<T, V> {
    void effectiveAsOfNow(@NonNull T item);

    void effectiveAsOf(@NonNull Instant asOf, @NonNull T item);

    void expireAsOfNow();

    void expireAsOf(@NonNull Instant expireAt);
}
