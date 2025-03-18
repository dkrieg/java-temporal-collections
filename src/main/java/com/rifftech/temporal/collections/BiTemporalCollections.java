package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.BiTemporalEventProducer;

import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class BiTemporalCollections {

    private static <T> ConcurrentSkipListBiTemporalCollection<T> createBiTemporalCollection(Collection<BiTemporalRecord<T>> temporalRecords) {
        SortedSet<BiTemporalRecord<T>> sorted = new TreeSet<>(temporalRecords);

        ConcurrentSkipListBiTemporalCollection<T> collection = new ConcurrentSkipListBiTemporalCollection<>();
        sorted.forEach(r -> {
            collection.effectiveAsOf(r.businessEffective().start(), r.systemEffective().start(), r.value());
            collection.expireAsOf(r.businessEffective().end(), r.systemEffective().end());
        });
        return collection;
    }

    public static <T> BiTemporalCollection<T> emptyBiTemporalCollection() {
        return createBiTemporalCollection(Collections.emptyList());
    }

    public static <T> BiTemporalCollection<T> immutableBiTemporalCollection(MutableBiTemporalCollection<T> collection) {
        return new ImmutableBiTemporalCollection<>(collection);
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
