package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;

/**
 * An interface representing a mutable bi-temporal collection that stores elements
 * of type {@code V}, where each element is associated with both a valid time range
 * and a transaction time range. This interface extends the functionality of
 * {@code MutableTemporalCollection} to add support for bi-temporal data management.
 * A bi-temporal collection allows querying and modifying elements based on two
 * independent timelines:
 * - Valid time: Represents when the data is considered valid or applicable.
 * - Transaction time: Represents when the data was recorded in the system.
 * The interface provides methods to retrieve stored elements based on specific
 * valid and transaction times.
 *
 * @param <T> the type of the value stored within the bi-temporal elements
 */
public interface MutableBiTemporalCollection<T> extends BiTemporalCollection<T> {

    /**
     * Associates the specified item with the current moment.
     *
     * @param item the item for which the currently valid temporal value is retrieved.
     *             Must not be null.
     * @return an {@code Optional} containing the previously effective temporal value ,
     * or an empty {@code Optional} if no such value exists.
     */
    default Optional<BiTemporalRecord<T>> effectiveAsOfNow(@NonNull T item) {
        return effectiveAsOf(Instant.now(), item);
    }

    /**
     * Marks the specified item as effective at the given valid time, updating the internal
     * temporal collection state accordingly.
     * If an item already exists the given valid time, its value is replaced and returned with no history of this
     * modification being made. Otherwise, the item is made effective at the given valid time and the prior,
     * now expired, version is returned.
     *
     * @param validTime the time at which the item should be marked as effective.
     *                  Must not be null.
     * @param item      the item to be marked as effective at the specified valid time.
     *                  Must not be null.
     * @return an {@code Optional} containing the prior temporal value that was effective
     * at the specified valid time, or an empty {@code Optional} if no prior
     * value was present.
     */
    default Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull T item) {
        return effectiveAsOf(validTime, Instant.now(), item);
    }

    /**
     * Retrieves an optional bi-temporal record that matches the specified valid time, transaction time, and item.
     * If the record exists and matches the provided criteria, the method returns it; otherwise, an empty optional is returned.
     *
     * @param validTime       the {@link Instant} representing the time at which the data is considered valid. Must not be null.
     * @param transactionTime the {@link Instant} representing the time at which the data was recorded in the system. Must not be null.
     * @param item            the item to be matched against the bi-temporal records. Must not be null.
     * @return an {@link Optional} containing the matching bi-temporal record, or an empty optional if no matching record is found.
     */
    Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime, @NonNull T item);

    /**
     * Marks the record as expired as of the current moment. This method internally utilizes the
     * {@code expireAsOf(Instant expireAt)} method with the current timestamp.
     *
     * @return an {@code Optional} containing the bi-temporal record that was marked as expired,
     *         or an empty {@code Optional} if no record was affected by the expiration.
     */
    default Optional<BiTemporalRecord<T>> expireAsOfNow() {
        return expireAsOf(Instant.now());
    }

    /**
     * Marks a bi-temporal record as expired as of the specified timestamp. This updates the
     * bi-temporal collection state to reflect the expiration of the record at the given moment.
     *
     * @param expireAt the {@link Instant} representing the point in time as of which the record
     *                 should be marked as expired. Must not be null.
     * @return an {@code Optional} containing the bi-temporal record that was marked as expired,
     *         or an empty {@code Optional} if no record was found and affected by the expiration.
     */
    Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant expireAt);
}
