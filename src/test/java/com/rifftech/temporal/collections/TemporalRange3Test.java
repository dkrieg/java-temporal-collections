package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemporalRange3Test {

    @Test
    void testOverlapsBefore() {
        Instant start1 = Instant.parse("2023-04-01T00:00:00Z");
        Instant end1 = Instant.parse("2023-05-01T00:00:00Z");
        Instant start2 = Instant.parse("2023-04-10T00:00:00Z");
        Instant end2 = Instant.parse("2023-04-30T00:00:00Z");

        TemporalRange range1 = TemporalRange.fromTo(start1, end1);
        TemporalRange range2 = TemporalRange.fromTo(start2, end2);

        assertTrue(range1.overlaps(range2));
    }

    @Test
    void testOverlapsAfter() {
        Instant start1 = Instant.parse("2023-04-10T00:00:00Z");
        Instant end1 = Instant.parse("2023-05-01T00:00:00Z");
        Instant start2 = Instant.parse("2023-04-11T00:00:00Z");
        Instant end2 = Instant.parse("2023-04-20T00:00:00Z");

        TemporalRange range1 = TemporalRange.fromTo(start1, end1);
        TemporalRange range2 = TemporalRange.fromTo(start2, end2);

        assertTrue(range1.overlaps(range2));
    }

    @Test
    void testNoOverlap() {
        Instant start1 = Instant.parse("2023-04-10T00:00:00Z");
        Instant end1 = Instant.parse("2023-05-01T00:00:00Z");
        Instant start2 = Instant.parse("2023-05-01T00:00:00Z");
        Instant end2 = Instant.parse("2023-06-01T00:00:00Z");

        TemporalRange range1 = TemporalRange.fromTo(start1, end1);
        TemporalRange range2 = TemporalRange.fromTo(start2, end2);

        assertFalse(range1.overlaps(range2));
    }

    @Test
    void testWithPrecision_Seconds() {
        Instant start = Instant.parse("2023-04-10T00:00:00.123Z");
        Instant end = Instant.parse("2023-05-01T00:00:00.456Z");

        TemporalRange range = TemporalRange.fromTo(start, end);
        TemporalRange rangeWithPrecision = range.withPrecision(ChronoUnit.SECONDS);

        assertEquals(Instant.parse("2023-04-10T00:00:00Z"), rangeWithPrecision.start());
        assertEquals(Instant.parse("2023-05-01T00:00:00Z"), rangeWithPrecision.end());
    }

    @Test
    void testWithPrecision_Millis() {
        Instant start = Instant.parse("2023-04-10T00:00:00.123456789Z");
        Instant end = Instant.parse("2023-05-01T00:00:00.987654321Z");

        TemporalRange range = TemporalRange.fromTo(start, end);
        TemporalRange rangeWithPrecision = range.withPrecision(ChronoUnit.MILLIS);

        assertEquals(Instant.parse("2023-04-10T00:00:00.123Z"), rangeWithPrecision.start());
        assertEquals(Instant.parse("2023-05-01T00:00:00.987Z"), rangeWithPrecision.end());
    }

    @Test
    void testIsContiguous_RangesAreContiguous() {
        Instant start1 = Instant.parse("2023-04-01T00:00:00Z");
        Instant end1 = Instant.parse("2023-05-01T00:00:00Z");
        Instant start2 = Instant.parse("2023-05-01T00:00:00Z");
        Instant end2 = Instant.parse("2023-06-01T00:00:00Z");

        TemporalRange range1 = TemporalRange.fromTo(start1, end1);
        TemporalRange range2 = TemporalRange.fromTo(start2, end2);

        List<TemporalRange> ranges = Arrays.asList(range1, range2);

        assertTrue(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testIsContiguous_RangesAreNotContiguous() {
        Instant start1 = Instant.parse("2023-04-01T00:00:00Z");
        Instant end1 = Instant.parse("2023-05-01T00:00:00Z");
        Instant start2 = Instant.parse("2023-05-02T00:00:00Z");
        Instant end2 = Instant.parse("2023-06-01T00:00:00Z");

        TemporalRange range1 = TemporalRange.fromTo(start1, end1);
        TemporalRange range2 = TemporalRange.fromTo(start2, end2);

        List<TemporalRange> ranges = Arrays.asList(range1, range2);

        assertFalse(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testIsContiguous_EmptyRanges() {
        List<TemporalRange> ranges = new ArrayList<>();

        assertTrue(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testIsContiguous_NullRanges() {
        assertTrue(TemporalRange.isContiguous(null));
    }

    @Test
    void testFromToMax_NullStart() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToMax(null));
        assertEquals("The start cannot be null.", exception.getMessage());
    }

    @Test
    void testFromToMax_ValidStart() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromToMax(start);
        assertEquals(start, range.start());
        assertEquals(TemporalRange.MAX, range.end());
    }

    @Test
    void testFromToMaxWithPrecision_NullStart() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToMax(null, ChronoUnit.SECONDS));
        assertEquals("The start cannot be null.", exception.getMessage());
    }

    @Test
    void testFromToMaxWithPrecision_NullPrecision() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToMax(Instant.now(), null));
        assertEquals("The precision cannot be null.", exception.getMessage());
    }

    @Test
    void testFromToMaxWithPrecision_ValidStartAndPrecision() {
        Instant start = Instant.now();
        TemporalRange range = TemporalRange.fromToMax(start, ChronoUnit.SECONDS);
        assertEquals(start.truncatedTo(ChronoUnit.SECONDS), range.start());
        assertEquals(TemporalRange.MAX.truncatedTo(ChronoUnit.SECONDS), range.end());
    }

    @Test
    void testFromToNow_NullStart() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToNow(null));
        assertEquals("The start cannot be null.", exception.getMessage());
    }

    @Test
    void testFromToNow_ValidStart() {
        Instant start = Instant.now().minusSeconds(100);
        TemporalRange range = TemporalRange.fromToNow(start);
        assertEquals(start, range.start());
        assertTrue(Instant.now().isAfter(range.start()) || Instant.now().equals(range.start()));  // As exact equality can't be ensured
    }

    @Test
    void testFromToNowWithPrecision_NullStart() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToNow(null, ChronoUnit.SECONDS));
        assertEquals("The start cannot be null.", exception.getMessage());
    }

    @Test
    void testFromToNowWithPrecision_NullPrecision() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToNow(Instant.now(), null));
        assertEquals("The precision cannot be null.", exception.getMessage());
    }

    @Test
    void testFromToNowWithPrecision_ValidStartAndPrecision() {
        Instant start = Instant.now().minusSeconds(100);
        TemporalRange range = TemporalRange.fromToNow(start, ChronoUnit.SECONDS);
        assertEquals(start.truncatedTo(ChronoUnit.SECONDS), range.start());
        assertTrue(Instant.now().truncatedTo(ChronoUnit.SECONDS).isAfter(range.start()) || Instant.now().truncatedTo(ChronoUnit.SECONDS).equals(range.start())); // As exact equality can't be ensured
    }
}
