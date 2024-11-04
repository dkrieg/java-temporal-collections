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
abstract class AbstractNavigableTemporalCollection<T extends TemporalObject> implements TemporalCollection<T> {
    NavigableMap<TemporalRange, T> items = new TreeMap<>(Comparator.reverseOrder());

    @Override
    public Optional<T> getAsOf(Instant validTime) {
        validateInstant(validTime);
        synchronized (items) {
            return items.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(validTime))
                    .map(Map.Entry::getValue)
                    .findFirst();
        }
    }

    @Override
    public Collection<T> getInRange(TemporalRange validTimeRange) {
        validateTemporalRange(validTimeRange);
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> validTimeRange.overlaps(e.getKey()))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public final void add(T item) {
        validateItem(item);
        TemporalRange validTimeRange = validTimeRange(item);
        validateTemporalRange(validTimeRange);
        checkOverlap(item, validTimeRange);
        items.put(validTimeRange, item);
    }

    @Override
    public final void remove(T item) {
        validateItem(item);
        TemporalRange validTimeRange = validTimeRange(item);
        validateTemporalRange(validTimeRange);
        items.entrySet().removeIf(entry -> validTimeRange.equals(entry.getKey()));

    }

    @Override
    public final int size() {
        return items.size();
    }

    @Override
    public final boolean isEmpty() {
        return items.isEmpty();
    }

    protected abstract TemporalRange validTimeRange(T item);

    void checkOverlap(T item, TemporalRange range) {
        for (Map.Entry<TemporalRange, T> entry : items.entrySet()) {
            if (entry.getValue().equals(item) && entry.getKey().overlaps(range)) {
                throw new IllegalArgumentException("Overlap detected with existing range: " + entry);
            }
        }
    }

    void validateTemporalRange(TemporalRange range) {
        if (range == null) {
            throw new IllegalArgumentException("TemporalRange cannot be null.");
        }
    }

    void validateItem(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
    }

    void validateInstant(Instant validTime) {
        if (validTime == null) {
            throw new IllegalArgumentException("Instant cannot be null.");
        }
    }
}
