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

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;

class BaseBiTemporalCollection<T> implements BiTemporalCollection<T> {
    protected final NavigableMap<BiTemporalRange, T> items = Collections.synchronizedNavigableMap(new TreeMap<>());

    /**
     * Retrieves an item from the collection that is valid as of the specified valid time and the current transaction time.
     *
     * @param validTime the point in time at which the item is considered valid; must not be null.
     * @return an {@code Optional<T>} containing the item valid at the specified valid time and the current transaction time, if one exists, otherwise an empty {@code Optional}.
     */
    public Optional<T> getAsOf(Instant validTime) {
        return findSingleValidItem(validTime, Instant.now());
    }

    /**
     * Retrieves an item from the collection that is valid as of the specified valid time and transaction time.
     *
     * @param validTime       the point in time at which the item is considered valid; must not be null.
     * @param transactionTime the point in time at which the transaction is considered valid; must not be null.
     * @return an {@code Optional<T>} containing the item valid at the specified valid and transaction times,
     * if one exists; otherwise, an empty {@code Optional}.
     */
    public Optional<T> getAsOf(Instant validTime, Instant transactionTime) {
        return findSingleValidItem(validTime, transactionTime);
    }

    /**
     * Retrieves all items from the collection that are valid within the specified valid time range.
     *
     * @param validTimeRange the temporal range during which the items are considered valid
     * @return a collection of items that are valid within the specified temporal range
     */
    public Collection<T> getInRange(TemporalRange validTimeRange) {
        return findItemsInRange(validTimeRange, FOREVER);
    }

    /**
     * Retrieves all items from the collection that are valid within the specified
     * valid time range and the specified transaction time range.
     *
     * @param validTimeRange       the temporal range during which the items are considered valid; must not be null
     * @param transactionTimeRange the temporal range during which the transaction is considered valid; must not be null
     * @return a collection of items that match the specified valid and transaction temporal ranges
     */
    public Collection<T> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        return findItemsInRange(validTimeRange, transactionTimeRange);
    }

    /**
     * Retrieves a collection of items from the collection that are valid as of the specified transaction time.
     *
     * @param transactionTime the point in time at which the transaction is considered valid; must not be null
     * @return a collection of items that are valid at the specified transaction time
     */
    public Collection<T> getTransactionAsOf(Instant transactionTime) {
        validateInstance(transactionTime);
        return items.entrySet().stream()
                .filter(entry -> entry.getKey().transactionTimeRange().contains(transactionTime))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the history of a specified item, represented as a collection of HistoricalRecord objects.
     *
     * @param item the item for which the historical records are being requested; must not be null
     * @return a collection of HistoricalRecord objects that capture the valid time ranges for the specified item
     */
    public Collection<HistoricalRecord<T>> getHistory(T item) {
        return getFullHistory(item).stream()
                .map(rec -> new HistoricalRecord<>(rec.item(), rec.timeRange().validTimeRange()))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the complete bi-temporal history of a specific item.
     *
     * @param item the item for which the full bi-temporal historical records are being requested; must not be null
     * @return a collection of {@code BiTemporalHistoricalRecord<T>} objects that capture both valid and transaction time ranges for the specified item
     */
    public Collection<BiTemporalHistoricalRecord<T>> getFullHistory(T item) {
        validateItem(item);
        return items.entrySet().stream()
                .filter(entry -> entry.getValue().equals(item))
                .map(entry -> new BiTemporalHistoricalRecord<>(entry.getValue(), entry.getKey()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the number of items in the collection.
     *
     * @return the number of items in this collection.
     */
    public int size() {
        return items.size();
    }

    /**
     * Checks if the collection is empty.
     *
     * @return {@code true} if the collection contains no items, {@code false} otherwise.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * Checks if the specified item is present in the collection.
     *
     * @param item the item to be checked for presence in the collection
     * @return true if the item is found in the collection, false otherwise
     */
    public boolean contains(T item) {
        return items.containsValue(item);
    }

    /**
     * Returns an iterator over elements of type {@code T} in this collection.
     *
     * @return an {@code Iterator<T>} over the elements in this collection, or an empty iterator if the collection is empty.
     */
    public Iterator<T> iterator() {
        return new SynchronizedIterator<>(items.values().iterator());
    }

    /**
     * Adds an item to the collection with the specified valid time range.
     *
     * @param item           the item to be added to the collection
     * @param validTimeRange the temporal range during which the item is considered valid
     */
    public void add(T item, TemporalRange validTimeRange) {
        add(item, validTimeRange, FOREVER);
    }

    /**
     * Adds an item to the collection with the specified valid and transaction time ranges.
     *
     * @param item                 the item to be added to the collection, must not be null
     * @param validTimeRange       the temporal range during which the item is considered valid, must not be null
     * @param transactionTimeRange the temporal range during which the transaction is valid, must not be null
     */
    public void add(T item, TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        validateItem(item);
        validateTemporalRange(validTimeRange, transactionTimeRange);
        BiTemporalRange key = new BiTemporalRange(validTimeRange, transactionTimeRange);
        checkOverlap(item, key);
        items.put(key, item);
    }

    /**
     * Deletes an item from the collection that matches the specified valid time range.
     *
     * @param item           the item to be deleted from the collection, must not be null
     * @param validTimeRange the temporal range during which the item is considered valid, must not be null
     */
    public void delete(T item, TemporalRange validTimeRange) {
        validateItem(item);
        validateTemporalRange(validTimeRange);
        items.entrySet().removeIf(entry -> entry.getValue().equals(item) && entry.getKey().validTimeRange().overlaps(validTimeRange));
    }

    /**
     * Deletes an item from the collection that matches the specified valid time range and transaction time range.
     *
     * @param item                 the item to be deleted from the collection, must not be null
     * @param validTimeRange       the temporal range during which the item is considered valid, must not be null
     * @param transactionTimeRange the temporal range during which the transaction is valid, must not be null
     */
    public void delete(T item, TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        validateItem(item);
        validateTemporalRange(validTimeRange, transactionTimeRange);
        items.entrySet().removeIf(entry -> entry.getValue().equals(item)
                && entry.getKey().overlaps(new BiTemporalRange(validTimeRange, transactionTimeRange)));
    }

    /**
     * Deletes all occurrences of the specified item from the bi-temporal collection.
     *
     * @param item the item to be deleted from the collection, must not be null
     */
    public void deleteAll(T item) {
        validateItem(item);
        items.entrySet().removeIf(entry -> entry.getValue().equals(item));
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

    Collection<T> findItemsInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        validateTemporalRange(validTimeRange, transactionTimeRange);
        synchronized (items) {
            return items.entrySet()
                    .stream()
                    .filter(e -> e.getKey().overlaps(new BiTemporalRange(validTimeRange, transactionTimeRange)))
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
    }
}
