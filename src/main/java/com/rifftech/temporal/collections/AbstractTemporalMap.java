package com.rifftech.temporal.collections;

import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
abstract class AbstractTemporalMap<K, V  extends TemporalObject, N extends AbstractNavigableTemporalCollection<V>> implements TemporalObjectMap<K, V> {
    Map<K, N> items = new HashMap<>();

    @Override
    public Optional<V> getAsOf(K key, Instant validTime) {
        return Optional.ofNullable(items.get(key)).flatMap(c -> c.getAsOf(validTime));
    }

    @Override
    public Collection<V> getInRange(K key, TemporalRange validTimeRange) {
        return Optional.ofNullable(items.get(key))
                .map(c -> c.getInRange(validTimeRange))
                .orElseGet(List::of);
    }

    @Override
    public void put(K key, V value) {
        items.computeIfAbsent(key, k -> newNavigableTemporalCollection()).add(value);
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

    protected abstract N newNavigableTemporalCollection();
}
