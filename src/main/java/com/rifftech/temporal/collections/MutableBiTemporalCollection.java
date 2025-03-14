package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Optional;

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
        return effectiveAsOfNow(item, ChronoUnit.NANOS);
    }

    default Optional<BiTemporalRecord<T>> effectiveAsOfNow(@NonNull T item, @NonNull TemporalUnit precision) {
        return effectiveAsOf(Instant.now(), item, precision);
    }

    /**
     * Marks the specified item as effective at the given valid time, updating the internal
     * temporal collection state accordingly.
     * If an item already exists the given valid time, its value is replaced and returned with no history of this
     * modification being made. Otherwise, the item is made effective at the given valid time and the prior,
     * now expired, version is returned.
     *
     * @param businessTime the time at which the item should be marked as effective.
     *                     Must not be null.
     * @param item         the item to be marked as effective at the specified valid time.
     *                     Must not be null.
     * @return an {@code Optional} containing the prior temporal value that was effective
     * at the specified valid time, or an empty {@code Optional} if no prior
     * value was present.
     */
    default Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant businessTime, @NonNull T item) {
        return effectiveAsOf(businessTime, item, ChronoUnit.NANOS);
    }

    default Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant businessTime, @NonNull T item, @NonNull TemporalUnit precision) {
        return effectiveAsOf(businessTime, Instant.now(), item, precision);
    }

    /**
     * Retrieves an optional bi-temporal record that matches the specified valid time, transaction time, and item.
     * If the record exists and matches the provided criteria, the method returns it; otherwise, an empty optional is returned.
     *
     * @param businessTime the {@link Instant} representing the time at which the data is considered valid. Must not be null.
     * @param systemTime   the {@link Instant} representing the time at which the data was recorded in the system. Must not be null.
     * @param item         the item to be matched against the bi-temporal records. Must not be null.
     * @return an {@link Optional} containing the matching bi-temporal record, or an empty optional if no matching record is found.
     */
    Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant businessTime, @NonNull Instant systemTime, @NonNull T item);

    default Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant businessTime, @NonNull Instant systemTime, @NonNull T item, @NonNull TemporalUnit precision) {
        return effectiveAsOf(businessTime.truncatedTo(precision), systemTime.truncatedTo(precision), item);
    }

    /**
     * Marks the record as expired as of the current moment. This method internally utilizes the
     * {@code expireAsOf(Instant expireAt)} method with the current timestamp.
     *
     * @return an {@code Optional} containing the bi-temporal record that was marked as expired,
     * or an empty {@code Optional} if no record was affected by the expiration.
     */
    default Optional<BiTemporalRecord<T>> expireAsOfNow() {
        return expireAsOfNow(ChronoUnit.NANOS);
    }

    default Optional<BiTemporalRecord<T>> expireAsOfNow(@NonNull TemporalUnit precision) {
        return expireAsOf(Instant.now().truncatedTo(precision));
    }

    /**
     * Marks a bi-temporal record as expired as of the specified timestamp. This updates the
     * bi-temporal collection state to reflect the expiration of the record at the given moment.
     *
     * @param expireAt the {@link Instant} representing the point in time as of which the record
     *                 should be marked as expired. Must not be null.
     * @return an {@code Optional} containing the bi-temporal record that was marked as expired,
     * or an empty {@code Optional} if no record was found and affected by the expiration.
     */
    default Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant expireAt) {
        return expireAsOf(expireAt, ChronoUnit.NANOS);
    }

    default Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant expireAt, @NonNull TemporalUnit precision) {
        return expireAsOf(expireAt, Instant.now(), precision);
    }

    /**
     * Marks a bi-temporal record as expired as of the specified business and system timestamps.
     * This updates the bi-temporal collection state to reflect the expiration of the record
     * at the given business and system times.
     *
     * @param businessTime the {@link Instant} representing the point in time for the business-valid period
     *                     as of which the record should be marked as expired. Must not be null.
     * @param systemTime   the {@link Instant} representing the point in time for the system-valid period
     *                     as of which the record should be marked as expired. Must not be null.
     * @return an {@code Optional} containing the bi-temporal record that was marked as expired,
     * or an empty {@code Optional} if no record was found and affected by the expiration.
     */
    Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant businessTime, @NonNull Instant systemTime);

    default Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant businessTime, @NonNull Instant systemTime, @NonNull TemporalUnit precision) {
        return expireAsOf(businessTime.truncatedTo(precision), systemTime.truncatedTo(precision));
    }
}
