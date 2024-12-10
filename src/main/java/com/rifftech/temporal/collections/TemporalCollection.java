package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface TemporalCollection<T, V extends TemporalValue<T>> {
    Optional<V> getAsOfNow();

    Optional<V> getAsOf(Instant validTime);

    Optional<V> getPriorToNow();

    Optional<V> getPriorTo(Instant validTime);

    Collection<V> getInRange(TemporalRange validTimeRange);

    int size();

    boolean isEmpty();
}
