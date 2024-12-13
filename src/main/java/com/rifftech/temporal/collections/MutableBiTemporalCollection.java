package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface MutableBiTemporalCollection<T> extends MutableTemporalCollection<T, BiTemporalValue<T>> {
    Optional<BiTemporalValue<T>> getAsOf(Instant validTime, Instant transactionTime);

    Optional<BiTemporalValue<T>> getPriorTo(Instant validTime, Instant transactionTime);

    Collection<BiTemporalValue<T>> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange);

    void effectiveAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime, @NonNull T item);
}
