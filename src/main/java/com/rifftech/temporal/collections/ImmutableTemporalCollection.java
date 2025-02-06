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
final class ImmutableTemporalCollection<T> implements TemporalCollection<T> {
    TemporalCollection<T> delegate;

    @Override
    public Optional<TemporalRecord<T>> getAsOfNow() {
        return delegate.getAsOfNow();
    }

    @Override
    public Optional<TemporalRecord<T>> getAsOf(@NonNull Instant validTime) {
        return delegate.getAsOf(validTime);
    }

    @Override
    public Optional<TemporalRecord<T>> getPriorToNow() {
        return delegate.getPriorToNow();
    }

    @Override
    public Optional<TemporalRecord<T>> getPriorTo(@NonNull Instant validTime) {
        return delegate.getPriorTo(validTime);
    }

    @Override
    public Collection<TemporalRecord<T>> getInRange(@NonNull TemporalRange validRange) {
        return delegate.getInRange(validRange);
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
