package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

public interface BiTemporalCollection<T extends BiTemporal> extends TemporalCollection<T> {

    Optional<T> getAsOf(Instant validTime, Instant transactionTime);

    Collection<T> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange);

    Collection<T> getHistory();
}
