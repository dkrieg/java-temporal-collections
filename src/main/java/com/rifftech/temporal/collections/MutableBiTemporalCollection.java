package com.rifftech.temporal.collections;

import lombok.NonNull;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

/**
 * Represents a mutable collection of bitemporal values, where each value is associated with both
 * a valid time range and a transaction time range. This interface extends the functionality of
 * {@link MutableTemporalCollection} to include additional methods for managing and querying
 * bitemporal data.
 *
 * @param <T> the type of value stored within the bitemporal collection
 */
public interface MutableBiTemporalCollection<T> extends MutableTemporalCollection<T, BiTemporalValue<T>> {
    /**
     * Retrieves the BiTemporalValue associated with the specified valid time and transaction time.
     *
     * @param validTime       the valid time used as a reference point for the lookup.
     *                        This indicates the effective date of the value.
     * @param transactionTime the transaction time used as a reference point for the lookup.
     *                        This indicates when the value was recorded or changed.
     * @return an Optional containing the BiTemporalValue found for the given validTime and transactionTime,
     * or an empty Optional if no such value exists.
     */
    Optional<BiTemporalValue<T>> getAsOf(Instant validTime, Instant transactionTime);

    /**
     * Retrieves the BiTemporalValue associated with the time combination
     * that immediately precedes the specified valid time and transaction time.
     *
     * @param validTime       the valid time used as a reference point for the lookup.
     *                        This indicates the effective date of the value.
     * @param transactionTime the transaction time used as a reference point for the lookup.
     *                        This indicates when the value was recorded or changed.
     * @return an Optional containing the BiTemporalValue found prior to the given validTime and transactionTime,
     * or an empty Optional if no such value exists.
     */
    Optional<BiTemporalValue<T>> getPriorTo(Instant validTime, Instant transactionTime);

    /**
     * Retrieves the BiTemporalValue associated with the second most recent valid time and transaction time
     * combination that precedes the specified valid time and transaction time.
     *
     * @param validTime       the valid time used as a reference point for the lookup.
     *                        This indicates the effective date of the value.
     * @param transactionTime the transaction time used as a reference point for the lookup.
     *                        This indicates when the value was recorded or changed.
     * @return an Optional containing the BiTemporalValue found for the second most recent
     *         validTime and transactionTime combination before the specified values,
     *         or an empty Optional if no such value exists.
     */
    Optional<BiTemporalValue<T>> getPriorToPriorTo(Instant validTime, Instant transactionTime);

    /**
     * @see #getPriorTo(Instant, Instant)
     */
    default Optional<BiTemporalValue<T>> getPriorToAsOf(Instant validTime, Instant transactionTime) {
        return getPriorTo(validTime, transactionTime);
    }

    /**
     * @see #getInRange(TemporalRange)
     */
    default Collection<BiTemporalValue<T>> getInRangeAsOfNow(TemporalRange validTimeRange) {
        return getInRange(validTimeRange);
    }

    /**
     * Retrieves a collection of BiTemporalValue objects that fall within the specified valid time range
     * and transaction time range.
     *
     * @param validTimeRange       the TemporalRange representing the range of valid times to filter the results.
     *                             This indicates the effective dates of the values.
     * @param transactionTimeRange the TemporalRange representing the range of transaction times to filter the results.
     *                             This indicates the recorded or modified dates of the values.
     * @return a collection of BiTemporalValue objects that satisfy the given valid and transaction time range criteria.
     */
    Collection<BiTemporalValue<T>> getInRange(TemporalRange validTimeRange, TemporalRange transactionTimeRange);

    /**
     * @see #getInRange(TemporalRange, TemporalRange)
     */
    default Collection<BiTemporalValue<T>> getInRangeAsOf(TemporalRange validTimeRange, TemporalRange transactionTimeRange) {
        return getInRange(validTimeRange, transactionTimeRange);
    }

    /**
     * Updates or sets the given item as effective at the specified valid and transaction times.
     * This method associates the provided item with the given temporal dimensions,
     * ensuring it is recorded as effective from the specified valid time and at the given transaction time.
     *
     * @param validTime       the valid time used as a reference point. It specifies the effective date for the item.
     * @param transactionTime the transaction time used as a reference point. It specifies when the item was
     *                        recorded or updated in the dataset.
     * @param item            the item to be associated with the specified valid time and transaction time. Must not be null.
     */
    void effectiveAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime, @NonNull T item);
}
