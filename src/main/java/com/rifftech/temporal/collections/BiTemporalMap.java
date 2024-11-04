package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface BiTemporalMap<K, V extends BiTemporal> extends TemporalMap<K, V> {

    Optional<V> getAsOf(K key, Instant validTime, Instant transactionTime);

    Collection<V> getInRange(K key, TemporalRange validTimeRange, TemporalRange transactionTimeRange);
}
