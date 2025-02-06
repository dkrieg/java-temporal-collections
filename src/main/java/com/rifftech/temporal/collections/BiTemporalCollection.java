package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;

/**
 * An interface representing a collection of bi-temporal values. A bi-temporal value is associated
 * with both a valid time and a transaction time. This collection provides mechanisms to query
 * values based on these two temporal dimensions.
 *
 * @param <T> the type of the value stored within the bi-temporal elements
 */
public interface BiTemporalCollection<T> {
    /**
     * Retrieves the temporal value that is currently valid as of the present moment,
     * if such a value exists.
     *
     * @return an {@code Optional} containing the value valid at the present moment,
     *         or an empty {@code Optional} if no such value exists.
     */
    default Optional<BiTemporalRecord<T>> getAsOfNow() {
        return getAsOf(Instant.now());
    }

    /**
     * Retrieves the temporal value that was valid at the specified point in time, if such a value exists.
     *
     * @param validTime the point in time for which to retrieve the valid temporal value.
     *                  This parameter must not be null.
     * @return an {@code Optional} containing the value valid at the specified time,
     *         or an empty {@code Optional} if no such value exists at that time.
     */
    default Optional<BiTemporalRecord<T>> getAsOf(@NonNull Instant validTime) {
        return getAsOf(validTime, Instant.now());
    }

    /**
     * Retrieves the value associated with the specified combination of valid time and transaction time,
     * if such a value exists.
     *
     * @param validTime the point in time representing the validity of the value. This parameter must not be null.
     * @param transactionTime the point in time representing the transaction time of the value. This parameter must not be null.
     * @return an {@code Optional} containing the value corresponding to the specified valid time and transaction time,
     *         or an empty {@code Optional} if no such value exists.
     */
    Optional<BiTemporalRecord<T>> getAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime);

    /**
     * Retrieves the temporal value that was valid immediately prior to the present moment,
     * if such a value exists.
     *
     * @return an {@code Optional} containing the most recent value that was valid prior to now,
     *         or an empty {@code Optional} if no such value exists.
     */
    default Optional<BiTemporalRecord<T>> getPriorToNow() {
        return getPriorTo(Instant.now());
    }

    /**
     * Retrieves the temporal value that was valid immediately prior to the specified point in time,
     * if such a value exists.
     *
     * @param validTime the point in time for which to retrieve the most recent temporal value
     *                  that was valid before it. This parameter must not be null.
     * @return an {@code Optional} containing the most recent value that was valid before
     *         the specified time, or an empty {@code Optional} if no such value exists.
     */
    default Optional<BiTemporalRecord<T>> getPriorTo(@NonNull Instant validTime) {
        return getPriorTo(validTime, Instant.now());
    }

    /**
     * Retrieves the bi-temporal value that was valid immediately prior to the specified combination
     * of valid time and transaction time, if such a value exists.
     *
     * @param validTime the point in time representing the validity of the value. This parameter must not be null.
     * @param transactionTime the point in time representing the transaction time of the value. This parameter must not be null.
     * @return an {@code Optional} containing the bi-temporal value that was valid prior to the specified valid
     *         time and transaction time, or an empty {@code Optional} if no such value exists.
     */
    Optional<BiTemporalRecord<T>> getPriorTo(@NonNull Instant validTime, @NonNull Instant transactionTime);

    /**
     * Retrieves a collection of temporal values that are valid within the specified temporal range.
     *
     * @param validRange the range of time for which to retrieve the valid temporal values.
     *                       This parameter must not be null.
     * @return a collection of temporal values that are valid within the specified range.
     * If no such values exist, an empty collection is returned.
     */
    Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange);

    /**
     * Retrieves a collection of bi-temporal values that intersect the specified ranges of valid time
     * and transaction time.
     *
     * @param validRange the range of valid time to filter the bi-temporal values. This range must not be null.
     * @param transactionRange the range of transaction time to filter the bi-temporal values. This range must not be null.
     * @return a collection of bi-temporal values that overlap with the specified valid and transaction time ranges.
     */
    Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange, @NonNull TemporalRange transactionRange);

    /**
     * Returns the number of temporal values currently stored in the collection.
     *
     * @return the number of temporal values in the collection
     */
    int size();

    /**
     * Checks if the collection is empty, meaning it contains no temporal values.
     *
     * @return true if the collection has no temporal values; false otherwise
     */
    boolean isEmpty();
}
