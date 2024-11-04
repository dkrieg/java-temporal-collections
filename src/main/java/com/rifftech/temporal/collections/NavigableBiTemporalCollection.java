package com.rifftech.temporal.collections;

import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
public class NavigableBiTemporalCollection<T extends BiTemporal> implements BiTemporalCollection<T> {
    NavigableMap<BiTemporalRange, T> items = new TreeMap<>(Comparator.reverseOrder());

    @Override
    public Optional<T> getAsOf(Instant validTime) {
        return findSingleValidItem(validTime, Instant.now());
    }

    @Override
    public Optional<T> getAsOf(Instant validTime, Instant transactionTime) {
        return findSingleValidItem(validTime, transactionTime);
    }

    @Override
    public Collection<T> getInRange(TemporalRange validTimeRange) {
        validateTemporalRange(validTimeRange);
        Instant now = Instant.now();
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> validTimeRange.overlaps(e.getKey().validTimeRange()) && e.getKey().transactionTimeRange().end().isAfter(now))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<T> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        validateTemporalRange(validTimeRange, transactionTimeRange);
        BiTemporalRange biTemporalRange = new BiTemporalRange(validTimeRange, transactionTimeRange);
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> biTemporalRange.overlaps(e.getKey()))
                    .map(Map.Entry::getValue)
                    .toList();
        }
    }

    @Override
    public Collection<T> getHistory() {
        Instant now = Instant.now();
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> e.getKey().transactionTimeRange().end().isBefore(now))
                    .map(Map.Entry::getValue)
                    .toList();
        }
    }

    @Override
    public void add(T item) {
        validateItem(item);
        validateTemporalRange(item.businessEffective(), item.systemEffective());
        BiTemporalRange key = new BiTemporalRange(item.businessEffective(), item.systemEffective());
        checkOverlap(item, key);
        items.put(key, item);
    }

    @Override
    public void remove(T item) {
        validateItem(item);
        validateTemporalRange(item.businessEffective(), item.systemEffective());
        BiTemporalRange timeRange = new BiTemporalRange(item.businessEffective(), item.systemEffective());
        items.entrySet().removeIf(entry ->  entry.getKey().equals(timeRange));
    }

    @Override
    public int size() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    void checkOverlap(T item, BiTemporalRange range) {
        for (Map.Entry<BiTemporalRange, T> entry : items.entrySet()) {
            if (!entry.getValue().equals(item) && entry.getKey().overlaps(range)) {
                throw new IllegalArgumentException("Overlap detected with existing range: " + entry);
            }
        }
    }

    void validateItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null.");
        }
    }

    void validateTemporalRange(TemporalRange... timeRange) {
        for (TemporalRange range : timeRange) {
            if (range == null) {
                throw new IllegalArgumentException("Temporal Range must not be null.");
            }
        }
    }

    void validateInstance(Instant... times) {
        for (Instant time : times) {
            if (time == null) {
                throw new IllegalArgumentException("Instance must not be null.");
            }
        }
    }

    Optional<T> findSingleValidItem(Instant validTime, Instant transactionTime) {
        validateInstance(validTime, transactionTime);
        return items.entrySet().stream()
                .filter(entry -> entry.getKey().isValidAt(validTime, transactionTime))
                .map(Map.Entry::getValue)
                .findFirst();
    }

}
