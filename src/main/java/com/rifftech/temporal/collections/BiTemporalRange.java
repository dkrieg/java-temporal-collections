package com.rifftech.temporal.collections;

import java.time.Instant;

/**
 * Represents a bitemporal range with both valid time and transaction time.
 * The class defines two temporal ranges:
 * <p>
 * - The valid time range represents the period during which the data is considered valid in the real world.
 * - The transaction time range represents the period during which the data is stored in the database.
 * <p>
 * Instances of this class are immutable and are comparable based on their valid and transaction time ranges.
 */
public record BiTemporalRange(TemporalRange validTimeRange,
                              TemporalRange transactionTimeRange) implements Comparable<BiTemporalRange> {
    /**
     * Compares this BiTemporalRange object with the specified BiTemporalRange for order.
     *
     * @param other the BiTemporalRange to be compared.
     * @return a negative integer, zero, or a positive integer as this BiTemporalRange is less than,
     * equal to, or greater than the specified BiTemporalRange based on valid time comparison first,
     * and transaction time comparison if the valid times are equal.
     */
    @Override
    public int compareTo(BiTemporalRange other) {
        int validTimeComparison = this.validTimeRange.compareTo(other.validTimeRange);
        if (validTimeComparison != 0) {
            return validTimeComparison;
        }
        return this.transactionTimeRange.compareTo(other.transactionTimeRange);
    }

    public boolean overlaps(BiTemporalRange range) {
        return validTimeRange.overlaps(range.validTimeRange) && transactionTimeRange.overlaps(range.transactionTimeRange);
    }

    public boolean isValidAt(Instant validTime, Instant transactionTime) {
        return validTimeRange.contains(validTime) && transactionTimeRange.contains(transactionTime);
    }
}