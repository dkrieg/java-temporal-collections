package com.rifftech.temporal.collections;

import java.time.Instant;

public record BiTemporalRange(TemporalRange validTimeRange,
                              TemporalRange transactionTimeRange) implements Comparable<BiTemporalRange> {
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
