package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

class BaseTemporalCollection<T> implements TemporalCollection<T> {
    protected final NavigableMap<TemporalRange, T> items = Collections.synchronizedNavigableMap(new TreeMap<>());

    /**
     * Retrieves the item in the temporal collection that is valid as of a specific point in time.
     *
     * @param validTime the point in time for which to retrieve the valid item; must not be null.
     * @return an {@code Optional<T>} containing the item valid at the specified time if one exists, otherwise an empty {@code Optional}.
     */
    public Optional<T> getAsOf(Instant validTime) {
        validateInstant(validTime);
        synchronized (items) {
            return items.entrySet().stream()
                    .filter(entry -> entry.getKey().contains(validTime))
                    .map(Map.Entry::getValue)
                    .findFirst();
        }
    }

    /**
     * Retrieves a collection of items that are valid within the specified temporal range.
     *
     * @param validTimeRange the temporal range for which to retrieve the valid items; must not be null.
     * @return a collection of items valid within the specified temporal range.
     */
    public Collection<T> getInRange(TemporalRange validTimeRange) {
        validateTemporalRange(validTimeRange);
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> e.getKey().overlaps(validTimeRange))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
    }

    /**
     * Retrieves the historical records of a specified item, including the item and its associated temporal ranges.
     *
     * @param item the item for which the historical records are to be retrieved; must not be null.
     * @return a collection of {@code HistoricalRecord<T>} objects representing the history of the specified item.
     * @throws IllegalArgumentException if the item is null.
     */
    public Collection<HistoricalRecord<T>> getHistory(T item) {
        validateItem(item);
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> e.getValue().equals(item))
                    .map(e -> new HistoricalRecord<>(e.getValue(), e.getKey()))
                    .collect(Collectors.toList());
        }
    }


    /**
     * Returns the number of items currently in the temporal collection.
     *
     * @return the number of items in the collection
     */
    public int size() {
        return items.size();
    }

    /**
     * Checks if the temporal collection is empty.
     *
     * @return {@code true} if the collection is empty, otherwise {@code false}.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Checks if the specified item is contained within the temporal collection.
     *
     * @param item the item to check for presence in the collection; must not be null.
     * @return {@code true} if the item is present in the collection, otherwise {@code false}.
     * @throws IllegalArgumentException if the item is null.
     */
    public boolean contains(T item) {
        validateItem(item);
        synchronized (items) {
            return items.containsValue(item);
        }
    }

    /**
     * Returns an iterator over the elements in the temporal collection.
     *
     * @return an {@code Iterator<T>} over the elements in the temporal collection. If the collection is empty, returns an empty iterator.
     */
    public Iterator<T> iterator() {
        return new SynchronizedIterator<>(items.values().iterator());
    }

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

    /**
     * Adds a given item to the temporal collection with an associated temporal range.
     *
     * @param item           the item to be added to the collection; must not be null.
     * @param validTimeRange the temporal range during which the item is considered valid; must not be null and must not overlap with existing ranges.
     * @throws IllegalArgumentException if the item is null.
     * @throws IllegalArgumentException if the temporal range is null.
     * @throws IllegalArgumentException if the temporal range overlaps with an existing range.
     */
    public void add(T item, TemporalRange validTimeRange) {
        validateItem(item);
        validateTemporalRange(validTimeRange);
        checkOverlap(item, validTimeRange);
        items.put(validTimeRange, item);
    }

    /**
     * Deletes the specified item from the collection within the given temporal range.
     * Only the portions of the item's temporal range that overlap with the given range will be removed.
     *
     * @param item           the item to be deleted from the collection; must not be null.
     * @param validTimeRange the temporal range during which the item is to be deleted; must not be null.
     * @throws IllegalArgumentException if the item is null.
     * @throws IllegalArgumentException if the temporal range is null.
     */
    public void delete(T item, TemporalRange validTimeRange) {
        validateItem(item);
        validateTemporalRange(validTimeRange);
        items.entrySet().removeIf(entry -> entry.getValue().equals(item) && entry.getKey().overlaps(validTimeRange));
    }

    /**
     * Deletes all instances of a specified item from the temporal collection across all time ranges.
     *
     * @param item the item to be deleted from the collection; must not be null.
     */
    public void deleteAll(T item) {
        validateItem(item);
        items.entrySet().removeIf(entry -> entry.getValue().equals(item));
    }
}
