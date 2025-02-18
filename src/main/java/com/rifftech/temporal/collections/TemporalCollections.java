package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.TemporalEventProducer;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

public class TemporalCollections {
    private static <T> ConcurrentSkipListTemporalCollection<T> createTemporalCollection(Collection<TemporalRecord<T>> temporalRecords) {
        SortedSet<TemporalRecord<T>> sorted = new TreeSet<>(temporalRecords);
        ConcurrentSkipListTemporalCollection<T> collection = new ConcurrentSkipListTemporalCollection<>();
        sorted.forEach(r -> {
            collection.effectiveAsOf(r.validRange().start(), r.value());
            collection.expireAsOf(r.validRange().end());
        });
        return collection;
    }

    public static <T> TemporalCollection<T> immutableTemporalCollection(MutableTemporalCollection<T> collection) {
        return new ImmutableTemporalCollection<>(collection);
    }

    public static <T> TemporalCollection<T> immutableTemporalCollection(Collection<TemporalRecord<T>> temporalRecords) {
        return new ImmutableTemporalCollection<T>(createTemporalCollection(temporalRecords));
    }

    public static <T> MutableTemporalCollection<T> mutableTemporalCollection(Collection<TemporalRecord<T>> temporalRecords) {
        return createTemporalCollection(temporalRecords);
    }

    public static <T> EventPublishingTemporalCollection<T> mutableTemporalCollection(Collection<TemporalRecord<T>> temporalRecords, TemporalEventProducer<T> producer) {
        return new EventPublishingTemporalCollection<>(createTemporalCollection(temporalRecords), producer);
    }
}
