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
class ImmutableBiTemporalCollection<T> {
    BiTemporalCollection<T> delegate;

    public Optional<BiTemporalRecord<T>> getAsOfNow() {
        return delegate.getAsOfNow();
    }

    public Optional<BiTemporalRecord<T>> getAsOf(@NonNull Instant validTime) {
        return delegate.getAsOf(validTime);
    }

    public Optional<BiTemporalRecord<T>> getAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime) {
        return delegate.getAsOf(validTime, transactionTime);
    }

    public Optional<BiTemporalRecord<T>> getPriorToNow() {
        return delegate.getPriorToNow();
    }

    public Optional<BiTemporalRecord<T>> getPriorTo(@NonNull Instant validTime) {
        return delegate.getPriorTo(validTime);
    }

    public Optional<BiTemporalRecord<T>> getPriorTo(@NonNull Instant validTime, @NonNull Instant transactionTime) {
        return delegate.getPriorTo(validTime, transactionTime);
    }

    public Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange) {
        return delegate.getInRange(validRange);
    }

    public Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange, @NonNull TemporalRange transactionRange) {
        return delegate.getInRange(validRange, transactionRange);
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }
}
