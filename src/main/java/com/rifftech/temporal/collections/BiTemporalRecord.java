package com.rifftech.temporal.collections;

public record BiTemporalRecord<T>(TemporalRange businessEffective, TemporalRange systemEffective,
                                  T value) implements Comparable<BiTemporalRecord<T>> {

    @Override
    public int compareTo(BiTemporalRecord<T> other) {
        int validRangeComparison = this.businessEffective().compareTo(other.businessEffective());
        if (validRangeComparison != 0) {
            return validRangeComparison;
        }
        return this.systemEffective.compareTo(other.systemEffective);
    }
}
