package com.rifftech.temporal.collections;

import lombok.NonNull;

/**
 * Represents a bi-temporal record with two temporal ranges: business-effective and
 * system-effective, along with a generic value. The business-effective range indicates
 * the period during which the value is effective from a business perspective, while the
 * system-effective range represents the period during which the value is stored within
 * the system.
 *
 * Instances of this class are immutable and can be compared to other instances
 * based on their temporal ranges.
 *
 * @param <T> The type of the associated value.
 */
public record BiTemporalRecord<T>(@NonNull TemporalRange businessEffective,
                                  @NonNull TemporalRange systemEffective,
                                  @NonNull T value) implements Comparable<BiTemporalRecord<T>> {

    @Override
    public int compareTo(BiTemporalRecord<T> other) {
        int validRangeComparison = this.businessEffective().compareTo(other.businessEffective());
        if (validRangeComparison != 0) {
            return validRangeComparison;
        }
        return this.systemEffective.compareTo(other.systemEffective);
    }
}
