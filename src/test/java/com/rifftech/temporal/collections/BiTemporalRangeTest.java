package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BiTemporalRangeTest {

    @Test
    public void testCompareTo_equal_validAndTransactionRanges() {
        TemporalRange validTemporalRange = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange = nowUntilMax(ChronoUnit.MILLIS);

        BiTemporalRange range1 = new BiTemporalRange(validTemporalRange, transactionTemporalRange);
        BiTemporalRange range2 = new BiTemporalRange(validTemporalRange, transactionTemporalRange);

        assertEquals(0, range1.compareTo(range2));
    }

    @Test
    public void testCompareTo_different_validAndTransactionRanges_greater() {
        TemporalRange nowUntilMax = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange differentTemporalRange = fromToMax(nowUntilMax.start().plus(Duration.ofSeconds(10)));

        BiTemporalRange range1 = new BiTemporalRange(nowUntilMax, nowUntilMax);
        BiTemporalRange range2 = new BiTemporalRange(differentTemporalRange, differentTemporalRange);

        assertTrue(range1.compareTo(range2) < 0);
    }

    @Test
    public void testCompareTo_different_validAndTransactionRanges_lesser() {
        TemporalRange nowUntilMax = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange differentTemporalRange = fromToMax(nowUntilMax.start().minus(Duration.ofSeconds(10)));

        BiTemporalRange range1 = new BiTemporalRange(nowUntilMax, nowUntilMax);
        BiTemporalRange range2 = new BiTemporalRange(differentTemporalRange, differentTemporalRange);

        assertTrue(range1.compareTo(range2) > 0);
    }

    @Test
    public void testOverlaps_overlapBothRanges() {
        TemporalRange validTemporalRange1 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange1 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange validTemporalRange2 = fromTo(validTemporalRange1.start().plus(Duration.ofSeconds(10)), validTemporalRange1.end().minus(Duration.ofSeconds(10)));
        TemporalRange transactionTemporalRange2 = fromTo(transactionTemporalRange1.start().plus(Duration.ofSeconds(10)), transactionTemporalRange1.end().minus(Duration.ofSeconds(10)));

        BiTemporalRange range1 = new BiTemporalRange(validTemporalRange1, transactionTemporalRange1);
        BiTemporalRange range2 = new BiTemporalRange(validTemporalRange2, transactionTemporalRange2);

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlaps_noOverlapForValidRanges() {
        TemporalRange validTemporalRange1 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange1 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange validTemporalRange2 = fromToMax(validTemporalRange1.start().plus(Duration.ofSeconds(10)));
        TemporalRange transactionTemporalRange2 = nowUntilMax(ChronoUnit.MILLIS);

        BiTemporalRange range1 = new BiTemporalRange(validTemporalRange1, transactionTemporalRange1);
        BiTemporalRange range2 = new BiTemporalRange(validTemporalRange2, transactionTemporalRange2);

        assertFalse(range1.overlaps(range2));
    }

    @Test
    public void testOverlaps_noOverlapForTransactionRanges() {
        TemporalRange validTemporalRange1 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange1 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange validTemporalRange2 = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange2 = fromToMax(transactionTemporalRange1.start().plus(Duration.ofSeconds(10)));

        BiTemporalRange range1 = new BiTemporalRange(validTemporalRange1, transactionTemporalRange1);
        BiTemporalRange range2 = new BiTemporalRange(validTemporalRange2, transactionTemporalRange2);

        assertFalse(range1.overlaps(range2));
    }

    @Test
    public void testIsValidAt_validAtBothTimes() {
        TemporalRange validTemporalRange = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange = nowUntilMax(ChronoUnit.MILLIS);

        BiTemporalRange range = new BiTemporalRange(validTemporalRange, transactionTemporalRange);

        assertTrue(range.isValidAt(validTemporalRange.start(), transactionTemporalRange.start()));
    }

    @Test
    public void testIsValidAt_validAtOnlyValidTime() {
        TemporalRange validTemporalRange = nowUntilMax(ChronoUnit.MILLIS);
        TemporalRange transactionTemporalRange = fromToMax(validTemporalRange.start().plus(Duration.ofSeconds(10)));

        BiTemporalRange range = new BiTemporalRange(validTemporalRange, transactionTemporalRange);

        assertFalse(range.isValidAt(validTemporalRange.start(), transactionTemporalRange.end().plus(Duration.ofSeconds(1))));
    }

    @Test
    public void testIsValidAt_validAtOnlyTransactionTime() {
        TemporalRange validTemporalRange = fromToMax(nowUntilMax(ChronoUnit.MILLIS).start().plus(Duration.ofSeconds(10)));
        TemporalRange transactionTemporalRange = nowUntilMax(ChronoUnit.MILLIS);

        BiTemporalRange range = new BiTemporalRange(validTemporalRange, transactionTemporalRange);

        assertFalse(range.isValidAt(validTemporalRange.end().plus(Duration.ofSeconds(1)), transactionTemporalRange.start()));
    }

    @Test
    public void testIsValidAt_invalidAtBothTimes() {
        TemporalRange validTemporalRange = fromToMax(nowUntilMax(ChronoUnit.MILLIS).start().plus(Duration.ofSeconds(10)));
        TemporalRange transactionTemporalRange = fromToMax(nowUntilMax(ChronoUnit.MILLIS).start().plus(Duration.ofSeconds(10)));

        BiTemporalRange range = new BiTemporalRange(validTemporalRange, transactionTemporalRange);

        assertFalse(range.isValidAt(validTemporalRange.end().plus(Duration.ofSeconds(1)), transactionTemporalRange.end().plus(Duration.ofSeconds(1))));
    }
}
