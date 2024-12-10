package com.rifftech.temporal.collections;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
final class ImmutableTemporalCollection<T, V extends TemporalValue<T>> implements TemporalCollection<T, V> {
    @NonNull
    MutableTemporalCollection<T, V> delegate;

    @Override
    public Optional<V> getAsOfNow() {
        return delegate.getAsOfNow();
    }

    @Override
    public Optional<V> getAsOf(Instant validTime) {
        return delegate.getAsOf(validTime);
    }

    @Override
    public Optional<V> getPriorToNow() {
        return delegate.getPriorToNow();
    }

    @Override
    public Optional<V> getPriorTo(Instant validTime) {
        return delegate.getPriorTo(validTime);
    }

    @Override
    public Collection<V> getInRange(TemporalRange validTimeRange) {
        return delegate.getInRange(validTimeRange);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
