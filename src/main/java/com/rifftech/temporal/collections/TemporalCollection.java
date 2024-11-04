package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface TemporalCollection<T extends TemporalObject> {

    default Optional<T> getAsOfNow() {
        return getAsOf(Instant.now());
    }

    Optional<T> getAsOf(Instant validTime);

    Collection<T> getInRange(TemporalRange validTimeRange);

    int size();

    boolean isEmpty();

    void add(T item);

    void remove(T item);
}
