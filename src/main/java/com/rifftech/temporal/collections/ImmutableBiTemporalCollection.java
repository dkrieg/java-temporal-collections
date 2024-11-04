package com.rifftech.temporal.collections;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
class ImmutableBiTemporalCollection<T extends BiTemporal> implements BiTemporalCollection<T> {
    BiTemporalCollection<T> delegate;

    @Override
    public Optional<T> getAsOf(Instant validTime, Instant transactionTime) {
        return delegate.getAsOf(validTime, transactionTime);
    }

    @Override
    public Collection<T> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        return delegate.getInRange(validTimeRange, transactionTimeRange);
    }

    @Override
    public Collection<T> getHistory() {
        return delegate.getHistory();
    }

    @Override
    public Optional<T> getAsOfNow() {
        return delegate.getAsOfNow();
    }

    @Override
    public Optional<T> getAsOf(Instant validTime) {
        return delegate.getAsOf(validTime);
    }

    @Override
    public Collection<T> getInRange(TemporalRange validTimeRange) {
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

    @Override
    public void add(T item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(T item) {
        throw new UnsupportedOperationException();
    }
}
