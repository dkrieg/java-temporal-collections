package com.rifftech.temporal.collections;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * Represents a collection of temporal objects where each object is associated
 * with both valid time range and transaction time range. This allows for the 
 * tracking of historical changes in both the state of the objects and the 
 * knowledge about when those states were recorded.
 *
 * @param <T> the type of elements in this bi-temporal collection
 */
public interface BiTemporalCollection<T> extends TemporalCollection<T> {
    /**
     * Retrieves an item from the bi-temporal collection that is valid as of the specified valid time and transaction time.
     *
     * @param validTime the point in time at which the item is considered valid; must not be null.
     * @param transactionTime the point in time at which the item's state is recorded; must not be null.
     * @return an {@code Optional<T>} containing the item valid at the specified valid time and transaction time, if one exists, otherwise an empty {@code Optional}.
     */
    Optional<T> getAsOf(Instant validTime, Instant transactionTime);

    /**
     * Retrieves a collection of items from the bi-temporal collection that are valid and recorded
     * within the specified valid time range and transaction time range.
     *
     * @param validTimeRange the range of time within which the items must be valid; must not be null.
     * @param transactionTimeRange the range of time within which the items' states must be recorded; must not be null.
     * @return a collection of items that are valid and recorded within the specified time ranges.
     */
    Collection<T> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange);

    /**
     * Retrieves a collection of items from the bi-temporal collection that are recorded 
     * as of the specified transaction time.
     *
     * @param transactionTime the point in time at which the item's state is recorded in the system; must not be null.
     * @return a collection of items that are recorded as of the specified transaction time.
     */
    Collection<T> getTransactionAsOf(Instant transactionTime);

    /**
     * Retrieves the full history of a specified item, including all its bi-temporal historical records.
     *
     * @param item the item for which the full history is to be retrieved; must not be null.
     * @return a collection of {@code BiTemporalHistoricalRecord<T>} objects representing the historical records of the specified item.
     */
    Collection<BiTemporalHistoricalRecord<T>> getFullHistory(T item);
}