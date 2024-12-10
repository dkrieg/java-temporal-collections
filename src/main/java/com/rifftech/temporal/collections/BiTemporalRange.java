package com.rifftech.temporal.collections;

import java.time.Instant;

public record BiTemporalRange(TemporalRange validTemporalRange,
                              TemporalRange transactionTemporalRange) implements Comparable<BiTemporalRange> {
    @Override
    public int compareTo(BiTemporalRange other) {
        int validTimeComparison = this.validTemporalRange.compareTo(other.validTemporalRange);
        if (validTimeComparison != 0) {
            return validTimeComparison;
        }
        return this.transactionTemporalRange.compareTo(other.transactionTemporalRange);
    }

    public boolean overlaps(BiTemporalRange range) {
        return validTemporalRange.overlaps(range.validTemporalRange) && transactionTemporalRange.overlaps(range.transactionTemporalRange);
    }

    public boolean isValidAt(Instant validTime, Instant transactionTime) {
        return validTemporalRange.contains(validTime) && transactionTemporalRange.contains(transactionTime);
    }
}
