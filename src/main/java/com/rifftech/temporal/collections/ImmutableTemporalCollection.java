package com.rifftech.temporal.collections;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

/**
 * A final class representing an immutable wrapper around a mutable temporal collection.
 * It delegates all operations to the underlying {@link MutableTemporalCollection}, ensuring
 * that the collection cannot be modified through this class.
 *
 * This class implements the {@link TemporalCollection} interface, allowing queries over
 * temporal values without exposing mutability. Operations for retrieving temporal values
 * such as those valid at specific times, within ranges, or prior values, are delegated
 * to the encapsulated mutable collection.
 *
 * @param <T> the type of the value stored within the temporal elements
 * @param <V> the type of the temporal value that extends {@link TemporalValue}
 */
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
