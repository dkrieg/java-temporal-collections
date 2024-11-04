package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class BiTemporalHashMap<K, V extends BiTemporal> implements BiTemporalMap<K, V> {
    Map<K, NavigableBiTemporalCollection<V>> items = new HashMap<>();

    @Override
    public Optional<V> getAsOf(K key, Instant validTime) {
        return Optional.ofNullable(items.get(key)).flatMap(c -> c.getAsOf(validTime));
    }

    @Override
    public Optional<V> getAsOf(K key, Instant validTime, Instant transactionTime) {
        return Optional.ofNullable(items.get(key)).flatMap(c -> c.getAsOf(validTime, transactionTime));

    }

    @Override
    public Collection<V> getInRange(K key, TemporalRange validTimeRange) {
        return Optional.ofNullable(items.get(key))
                .map(c -> c.getInRange(validTimeRange))
                .orElseGet(List::of);
    }

    @Override
    public Collection<V> getInRange(K key, TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        return Optional.ofNullable(items.get(key))
                .map(c -> c.getInRange(validTimeRange, transactionTimeRange))
                .orElseGet(List::of);
    }

    @Override
    public void put(K key, V value) {
        get(key).add(value);

    }

    @Override
    public void remove(K key, TemporalRange validTimeRange) {
        Optional.ofNullable(items.get(key)).ifPresent(c -> c.getInRange(validTimeRange).forEach(c::remove));
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public int size(K key) {
        return items.get(key).size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public boolean containsKey(K key) {
        return items.containsKey(key);
    }

    @Override
    public Set<K> keySet() {
        return items.keySet();
    }

    NavigableBiTemporalCollection<V> get(K key) {
        return items.computeIfAbsent(key, k -> new NavigableBiTemporalCollection<>());
    }
}
