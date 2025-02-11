package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.BiTemporalEventProducer;
import com.rifftech.temporal.events.TemporalEventProducer;

import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    private static <T> ConcurrentSkipListBiTemporalCollection<T> createBiTemporalCollection(Collection<BiTemporalRecord<T>> temporalRecords) {
        SortedSet<BiTemporalRecord<T>> sorted = new TreeSet<>(temporalRecords);

        ConcurrentSkipListBiTemporalCollection<T> collection = new ConcurrentSkipListBiTemporalCollection<>();
        sorted.forEach(r -> {
            collection.effectiveAsOf(r.businessEffective().start(), r.systemEffective().start(), r.value());
            collection.expireAsOf(r.businessEffective().end(), r.systemEffective().end());
        });
        return collection;
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

    public static <T> BiTemporalCollection<T> immutableBiTemporalCollection(Collection<BiTemporalRecord<T>> temporalRecords) {
        return new ImmutableBiTemporalCollection<>(createBiTemporalCollection(temporalRecords));
    }

    public static <T> MutableBiTemporalCollection<T> mutableBiTemporalCollection(Collection<BiTemporalRecord<T>> temporalRecords) {
        return createBiTemporalCollection(temporalRecords);
    }

    public static <T> EventPublishingBiTemporalCollection<T> mutableBiTemporalCollection(Collection<BiTemporalRecord<T>> temporalRecords, BiTemporalEventProducer<T> producer) {
        return new EventPublishingBiTemporalCollection<>(createBiTemporalCollection(temporalRecords), producer);
    }
}
