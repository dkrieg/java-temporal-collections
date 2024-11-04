package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface TemporalMap<K, V extends TemporalObject> {

    default Optional<V> getAsOfNow(K key) {
        return getAsOf(key, Instant.now());
    }

    Optional<V> getAsOf(K key, Instant validTime);

    Collection<V> getInRange(K key, TemporalRange validTimeRange);

    int size();

    int size(K key);

    boolean isEmpty();

    boolean containsKey(K key);

    Set<K> keySet();

    void put(K key, V value);

    void remove(K key, TemporalRange validTimeRange);
}
