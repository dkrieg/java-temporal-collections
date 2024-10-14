package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * Provides a collection of temporal objects where each object is associated with a specific time range.
 *
 * @param <T> the type of elements in this temporal collection
 */
public interface TemporalCollection<T> extends Iterable<T> {

    /**
     * Retrieves the item in the temporal collection that is valid as of the current moment.
     *
     * @return an {@code Optional<T>} containing the item valid at the current time if one exists, otherwise an empty {@code Optional}.
     */
    default Optional<T> getAsOfNow() {
        return getAsOf(Instant.now());
    }

    /**
     * Retrieves the item in the temporal collection that is valid as of a specific point in time.
     *
     * @param validTime the point in time for which to retrieve the valid item; must not be null.
     * @return an {@code Optional<T>} containing the item valid at the specified time if one exists, otherwise an empty {@code Optional}.
     */
    Optional<T> getAsOf(Instant validTime);

    /**
     * Retrieves a collection of items that are valid within the specified temporal range.
     *
     * @param validTimeRange the temporal range for which to retrieve the valid items; must not be null.
     * @return a collection of items valid within the specified temporal range.
     */
    Collection<T> getInRange(TemporalRange validTimeRange);

    /**
     * Retrieves the historical records of a specified item, including the item and its associated temporal ranges.
     *
     * @param item the item for which the historical records are to be retrieved; must not be null.
     * @return a collection of {@code HistoricalRecord<T>} objects representing the history of the specified item.
     */
    Collection<HistoricalRecord<T>> getHistory(T item);

    /**
     * Returns the number of items currently in the temporal collection.
     *
     * @return the number of items in the collection.
     */
    int size();

    /**
     * Checks if the temporal collection is empty.
     *
     * @return {@code true} if the collection is empty, otherwise {@code false}.
     */
    boolean isEmpty();

    /**
     * Checks if the specified item is contained within the temporal collection.
     *
     * @param item the item to check for presence in the collection; must not be null.
     * @return {@code true} if the item is present in the collection, otherwise {@code false}.
     */
    boolean contains(T item);

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an {@code Iterator<T>} over the elements in the temporal collection.
     */
    @Override
    Iterator<T> iterator();
}