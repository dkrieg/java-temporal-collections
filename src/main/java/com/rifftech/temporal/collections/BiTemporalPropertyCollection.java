package com.rifftech.temporal.collections;

public interface BiTemporalPropertyCollection<T> extends BiTemporalCollection<T>, TemporalPropertyCollection<T> {
    /**
     * Adds an item to the bi-temporal collection with the specified valid time range and transaction time range.
     *
     * @param item the item to be added to the collection
     * @param validTimeRange the temporal range within which the item is considered valid
     * @param transactionTimeRange the temporal range during which the item's state is recorded in the system
     */
    void add(T item, TemporalRange validTimeRange, TemporalRange transactionTimeRange);

    /**
     * Deletes an item from the bi-temporal collection with the specified valid time range
     * and transaction time range.
     *
     * @param item the item to be deleted from the collection
     * @param validTimeRange the temporal range within which the item is considered valid
     * @param transactionTimeRange the temporal range during which the item's state is recorded in the system
     */
    void delete(T item, TemporalRange validTimeRange, TemporalRange transactionTimeRange);

}
