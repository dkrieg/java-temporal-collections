package com.rifftech.temporal.collections;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
class ImmutableBiTemporalMap<K, V extends BiTemporal> implements BiTemporalMap<K, V> {
    BiTemporalMap<K, V> delegate;

    @Override
    public Optional<V> getAsOf(K key, Instant validTime, Instant transactionTime) {
        return delegate.getAsOf(key, validTime, transactionTime);
    }

    @Override
    public Collection<V> getInRange(K key, TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        return delegate.getInRange(key, validTimeRange, transactionTimeRange);
    }

    @Override
    public Optional<V> getAsOfNow(K key) {
        return delegate.getAsOfNow(key);
    }

    @Override
    public Optional<V> getAsOf(K key, Instant validTime) {
        return delegate.getAsOf(key, validTime);
    }

    @Override
    public Collection<V> getInRange(K key, TemporalRange validTimeRange) {
        return delegate.getInRange(key, validTimeRange);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public int size(K key) {
        return delegate.size(key);
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return delegate.containsKey(key);
    }

    @Override
    public Set<K> keySet() {
        return delegate.keySet();
    }

    @Override
    public void put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(K key, TemporalRange validTimeRange) {
        throw new UnsupportedOperationException();
    }
}
