package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemporalRangeTest {

    @Test
    public void testTemporalRange_WithNullStartAndEnd() {
        assertThrows(IllegalArgumentException.class, () -> new TemporalRange(null, Instant.now()));
        assertThrows(IllegalArgumentException.class, () -> new TemporalRange(Instant.now(), null));
    }

    @Test
    public void testContains_WithInstantWithinRange() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertTrue(range.contains(start.plusSeconds(5)));
    }

    @Test
    public void testContains_WithInstantBeforeRange() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertFalse(range.contains(start.minusSeconds(5)));
    }

    @Test
    public void testContains_WithInstantAfterRange() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertFalse(range.contains(end.plusSeconds(5)));
    }

    @Test
    public void testContains_WithInstantEqualStart() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertTrue(range.contains(start));
    }

    @Test
    public void testContains_WithInstantEqualEnd() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertFalse(range.contains(end));
    }

    @Test
    public void testContains_WithNullInstant() {
        TemporalRange range = TemporalRange.nowFor(Duration.ofSeconds(10));

        assertFalse(range.contains(null));
    }

    @Test
    public void testOverlaps_WithOverlapAtStart() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.minusSeconds(5), end.minusSeconds(5));

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlaps_WithOverlapAtEnd() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(5), end.plusSeconds(5));

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlaps_WithOverlapInMiddle() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(3), end.minusSeconds(3));

        assertTrue(range1.overlaps(range2));
    }

    @Test
    public void testOverlaps_WithoutOverlap() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(15), end.plusSeconds(15));

        assertFalse(range1.overlaps(range2));
        assertFalse(range2.overlaps(range1));
    }

    @Test
    public void testOverlaps_WithNullRange() {
        TemporalRange range1 = TemporalRange.nowUntilMax();

        assertFalse(range1.overlaps(null));
    }

    @Test
    public void testIsBefore_WithTemporalRangeAfter() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(15), end.plusSeconds(15));

        assertTrue(range1.isBefore(range2));
    }

    @Test
    public void testIsBefore_WithTemporalRangeBefore() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.minusSeconds(15), end.minusSeconds(15));

        assertFalse(range1.isBefore(range2));
    }

    @Test
    public void testIsBefore_WithTemporalRangeOverlaps() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(5), end.plusSeconds(5));

        assertFalse(range1.isBefore(range2));
    }

    @Test
    public void testIsBefore_WithNullTemporalRange() {
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofSeconds(10));

        assertThrows(IllegalArgumentException.class, () -> range1.isBefore(null));
    }

    @Test
    public void testIsAfter_WithTemporalRangeBefore() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.minusSeconds(15), end.minusSeconds(15));

        assertTrue(range1.isAfter(range2));
    }

    @Test
    public void testIsAfter_WithTemporalRangeAfter() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(15), end.plusSeconds(15));

        assertFalse(range1.isAfter(range2));
    }

    @Test
    public void testIsAfter_WithTemporalRangeOverlaps() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(5), end.plusSeconds(5));

        assertFalse(range1.isAfter(range2));
    }

    @Test
    public void testIsAfter_WithNullTemporalRange() {
        TemporalRange range1 = TemporalRange.nowFor(Duration.ofSeconds(10));

        assertThrows(IllegalArgumentException.class, () -> range1.isAfter(null));
    }

    @Test
    public void testIsContiguousWith_contiguousAtStart() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(end, end.plusSeconds(10));

        assertTrue(range1.isContiguousWith(range2));
    }


    @Test
    public void testIsContiguousWith_contiguousAtEnd() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.minusSeconds(10), start);

        assertTrue(range1.isContiguousWith(range2));
    }


    @Test
    public void testIsContiguousWith_nonContiguous() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end);
        TemporalRange range2 = TemporalRange.fromTo(start.plusSeconds(15), end.plusSeconds(15));

        assertFalse(range1.isContiguousWith(range2));
    }


    @Test
    public void testIsContiguousWith_nullRange() {
        TemporalRange range = TemporalRange.nowFor(Duration.ofSeconds(10));

        assertFalse(range.isContiguousWith(null));
    }

    @Test
    public void testDurationInSeconds_WithPositiveDuration() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(10);
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertEquals(10, range.durationInSeconds());
    }

    @Test
    public void testDurationInSeconds_WithZeroDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(1));

        assertEquals(1, range.durationInSeconds());
    }

    @Test
    public void testDurationInSeconds_WithLargeDuration() {
        Instant start = Instant.EPOCH;
        Instant end = Instant.MAX;
        TemporalRange range = TemporalRange.fromTo(start, end);

        assertTrue(range.durationInSeconds() > 0);
        assertTrue(range.durationInSeconds() < Long.MAX_VALUE);
    }

    @Test
    public void testCompareTo_WithEqualStartAndEnd() {
        Instant start = Instant.now();
        TemporalRange range1 = TemporalRange.fromTo(start, start.plusSeconds(10));
        TemporalRange range2 = TemporalRange.fromTo(start, start.plusSeconds(10));

        assertEquals(0, range1.compareTo(range2));
    }

    @Test
    public void testCompareTo_WithDifferentStartSameEnd() {
        Instant start1 = Instant.now();
        Instant end = start1.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start1, end);

        Instant start2 = start1.plusSeconds(5);
        TemporalRange range2 = TemporalRange.fromTo(start2, end);

        assertTrue(range1.compareTo(range2) < 0);
    }

    @Test
    public void testCompareTo_WithSameStartDifferentEnd() {
        Instant start = Instant.now();
        Instant end1 = start.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start, end1);

        Instant end2 = end1.plusSeconds(5);
        TemporalRange range2 = TemporalRange.fromTo(start, end2);

        assertTrue(range1.compareTo(range2) < 0);
    }

    @Test
    public void testCompareTo_WithDifferentStartAndEnd() {
        Instant start1 = Instant.now();
        Instant end1 = start1.plusSeconds(10);
        TemporalRange range1 = TemporalRange.fromTo(start1, end1);

        Instant start2 = start1.plusSeconds(5);
        Instant end2 = end1.plusSeconds(5);
        TemporalRange range2 = TemporalRange.fromTo(start2, end2);

        assertTrue(range1.compareTo(range2) < 0);
    }

    // test get start date
    @Test
    public void testGetStartDate_WithPositiveDuration() {
        Instant now = Instant.now();
        TemporalRange range = TemporalRange.fromTo(now, now.plusSeconds(10));

        assertEquals(now, range.start());
    }

    @Test
    public void testGetStartDate_WithMaxDuration() {
        Instant now = Instant.now();
        TemporalRange range = TemporalRange.fromTo(now, Instant.MAX);

        assertEquals(now, range.start());
    }

    @Test
    public void testGetStartDate_WithNullStartDate() {
        TemporalRange range = TemporalRange.FOREVER;

        assertEquals(Instant.MIN, range.start());
    }

    @Test
    public void testGetEndDate_WithPositiveDuration() {
        Instant now = Instant.now();
        TemporalRange range = TemporalRange.fromTo(now, now.plusSeconds(10));

        assertEquals(now.plusSeconds(10), range.end());
    }

    @Test
    public void testGetEndDate_WithMaxDuration() {
        Instant now = Instant.now();
        TemporalRange range = TemporalRange.fromTo(now, Instant.MAX);

        assertEquals(Instant.MAX, range.end());
    }

    @Test
    public void testGetEndDate_WithNullEndDate() {
        TemporalRange range = TemporalRange.FOREVER;

        assertEquals(Instant.MAX, range.end());
    }

    @Test
    public void testnowUntil_WithFutureEnd() {
        TemporalRange range = TemporalRange.nowUntil(Instant.MAX);
        Instant now = Instant.now();

        assertTrue(range.start().isBefore(now.plusSeconds(1)));
        assertEquals(range.end(), Instant.MAX);
    }

    @Test
    public void testnowUntil_WithPastEnd() {
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowUntil(Instant.now().minusSeconds(10)));
    }

    @Test
    public void testnowUntil_WithCurrentEnd() {
        Instant now = Instant.now();
        TemporalRange range = TemporalRange.nowUntil(now.plusSeconds(1));

        assertTrue(range.start().isBefore(now.plusSeconds(1)));
        assertTrue(range.end().isBefore(now.plusSeconds(2)));
    }

    @Test
    public void testContains_NullValue() {
        TemporalRange range = TemporalRange.nowFor(Duration.ofSeconds(10));
        assertFalse(range.contains(null));
    }

    @Test
    public void testContains_ValueWithinRange() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertTrue(range.contains(start.plusSeconds(5)));
    }

    @Test
    public void testContains_ValueEqualToStart() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertTrue(range.contains(start));
    }

    @Test
    public void testContains_ValueEqualToEnd() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertFalse(range.contains(start.plusSeconds(10)));
    }

    @Test
    public void testContains_ValueBeforeStart() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertFalse(range.contains(start.minusSeconds(5)));
    }

    @Test
    public void testContains_ValueAfterEnd() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertFalse(range.contains(start.plusSeconds(15)));
    }

    @Test
    public void testUntilNowFrom_WithPastStart() {
        Instant start = Instant.now().minusSeconds(10);
        TemporalRange range = TemporalRange.fromToNow(start);
        assertTrue(range.start().isBefore(Instant.now()) && range.end().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    public void testUntilNowFrom_WithCurrentStart() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromToNow(start);
        assertTrue(range.start().equals(start) && range.end().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    public void testPlusWithDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        TemporalRange newRange = range.plus(Duration.ofSeconds(5));

        assertEquals(start.plusSeconds(5), newRange.start());
        assertEquals(start.plusSeconds(15), newRange.end());
    }

    @Test
    public void testPlusWithLargeDuration() {
        Instant start = Instant.EPOCH;
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertThrows(DateTimeException.class, () -> range.plus(Duration.ofSeconds(Long.MAX_VALUE)));
    }

    @Test
    public void testPlusWithNegativeDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));

        assertThrows(IllegalArgumentException.class, () -> range.plus(Duration.ofSeconds(-5)));
    }

    @Test
    public void testPlusWithZeroDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        TemporalRange newRange = range.plus(Duration.ZERO);

        assertEquals(start, newRange.start());
        assertEquals(range.end(), newRange.end());
    }
    @Test
    public void testMinusWithDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        TemporalRange newRange = range.minus(Duration.ofSeconds(5));

        assertEquals(start.minusSeconds(5), newRange.start());
        assertEquals(start.plusSeconds(5), newRange.end());
    }

    @Test
    public void testMinusWithLargeDuration() {
        Instant start = Instant.EPOCH;
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        assertThrows(DateTimeException.class, () -> range.minus(Duration.ofSeconds(Long.MAX_VALUE)));
    }

    @Test
    public void testMinusWithNegativeDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));

        assertThrows(IllegalArgumentException.class, () -> range.minus(Duration.ofSeconds(-5)));
    }

    @Test
    public void testMinusWithZeroDuration() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromTo(start, start.plusSeconds(10));
        TemporalRange newRange = range.minus(Duration.ZERO);

        assertEquals(start, newRange.start());
        assertEquals(range.end(), newRange.end());
    }
}
