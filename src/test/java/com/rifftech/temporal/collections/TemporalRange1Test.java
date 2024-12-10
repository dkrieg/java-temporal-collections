package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemporalRange1Test {

    @Test
    void testParseWithValidInput() {
        String input = "2024-10-30T13:00:00Z to 2024-10-30T14:00:00Z";
        TemporalRange result = TemporalRange.parse(input);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(Instant.parse("2024-10-30T13:00:00Z"), result.start());
        Assertions.assertEquals(Instant.parse("2024-10-30T14:00:00Z"), result.end());
    }

    @Test
    void testParseWithInvalidInput() {
        String input = "Invalid Input";
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.parse(input));
    }

    @Test
    void testParseWithNullStart() {
        String input = " to 2024-10-30T14:00:00Z";
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.parse(input));
    }

    @Test
    void testParseWithNullEnd() {
        String input = "2024-10-30T13:00:00Z to ";
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.parse(input));
    }

    @Test
    void testParseWithUnorderedRange() {
        String input = "2024-10-30T14:00:00Z to 2024-10-30T13:00:00Z";
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.parse(input));
    }

    @Test
    void testParseWithInvalidEndFormat() {
        String input = "2024-10-30T14:00:00Z to 2024-10-30T13:00:00";
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.parse(input));
    }

    @Test
    void testIsContiguousWithEmptyList() {
        Collection<TemporalRange> ranges = Collections.emptyList();
        Assertions.assertTrue(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testIsContiguousWithSingleElement() {
        Collection<TemporalRange> ranges = Collections.singletonList(
                new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"))
        );
        Assertions.assertTrue(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testIsContiguousWithAdjacentRanges() {
        Collection<TemporalRange> ranges = List.of(
                new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z")),
                new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"))
        );
        Assertions.assertTrue(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testIsContiguousWithNonAdjacentRanges() {
        Collection<TemporalRange> ranges = List.of(
                new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z")),
                new TemporalRange(Instant.parse("2022-03-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"))
        );
        Assertions.assertFalse(TemporalRange.isContiguous(ranges));
    }

    @Test
    void testDurationInSecondsWithHourDifference() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-01-01T01:00:00Z"));
        Assertions.assertEquals(3600, range.durationInSeconds());
    }

    @Test
    void testDurationInSecondsWithMinuteDifference() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-01-01T00:01:00Z"));
        Assertions.assertEquals(60, range.durationInSeconds());
    }

    @Test
    void testDurationInSecondsWithSecondDifference() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-01-01T00:00:01Z"));
        Assertions.assertEquals(1, range.durationInSeconds());
    }

    @Test
    void testContainsWithInstantWithinRange() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Instant testInstant = Instant.parse("2022-01-15T00:00:00Z");
        Assertions.assertTrue(range.contains(testInstant));
    }

    @Test
    void testContainsWithInstantOutsideRange() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Instant testInstant = Instant.parse("2022-03-01T00:00:00Z");
        Assertions.assertFalse(range.contains(testInstant));
    }

    @Test
    void testContainsWithInstantEqualToStart() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Instant testInstant = Instant.parse("2022-01-01T00:00:00Z");
        Assertions.assertTrue(range.contains(testInstant));
    }

    @Test
    void testContainsWithInstantEqualToEnd() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Instant testInstant = Instant.parse("2022-02-01T00:00:00Z");
        Assertions.assertFalse(range.contains(testInstant));
    }

    @Test
    void testPlusWithNullDuration() {
        TemporalRange range = new TemporalRange(Instant.parse("2024-10-30T13:00:00Z"), Instant.parse("2024-10-30T14:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> range.plus(null));
    }

    @Test
    void testPlusWithDuration() {
        TemporalRange range = new TemporalRange(Instant.parse("2024-10-30T13:00:00Z"), Instant.parse("2024-10-30T14:00:00Z"));
        TemporalRange result = range.plus(Duration.ofHours(1));
        Assertions.assertEquals(Instant.parse("2024-10-30T14:00:00Z"), result.start());
        Assertions.assertEquals(Instant.parse("2024-10-30T15:00:00Z"), result.end());
    }

    @Test
    void testPlusWithNegativeDuration() {
        TemporalRange range = new TemporalRange(Instant.parse("2024-10-30T13:00:00Z"), Instant.parse("2024-10-30T14:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> range.plus(Duration.ofHours(-1)));
    }

    @Test
    void testMinusWithNullDuration() {
        TemporalRange range = new TemporalRange(Instant.parse("2024-10-30T13:00:00Z"), Instant.parse("2024-10-30T14:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> range.minus(null));
    }

    @Test
    void testMinusWithDuration() {
        TemporalRange range = new TemporalRange(Instant.parse("2024-10-30T14:00:00Z"), Instant.parse("2024-10-30T15:00:00Z"));
        TemporalRange result = range.minus(Duration.ofHours(1));
        Assertions.assertEquals(Instant.parse("2024-10-30T13:00:00Z"), result.start());
        Assertions.assertEquals(Instant.parse("2024-10-30T14:00:00Z"), result.end());
    }

    @Test
    void testMinusWithNegativeDuration() {
        TemporalRange range = new TemporalRange(Instant.parse("2024-10-30T13:00:00Z"), Instant.parse("2024-10-30T14:00:00Z"));
        assertThrows(IllegalArgumentException.class, () -> range.minus(Duration.ofHours(-1)));
    }

    @Test
    void testIsBeforeWithRangeBefore() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:01Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertTrue(range.isBefore(otherRange));
    }

    @Test
    void testIsBeforeWithRangeAfter() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Assertions.assertFalse(range.isBefore(otherRange));
    }

    @Test
    void testIsBeforeWithOverlap() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"));
        Assertions.assertFalse(range.isBefore(otherRange));
    }

    @Test
    void testIsAfterWithRangeAfter() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-02-01T00:00:01Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Assertions.assertTrue(range.isAfter(otherRange));
    }

    @Test
    void testIsAfterWithRangeBefore() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:01Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertFalse(range.isAfter(otherRange));
    }

    @Test
    void testIsAfterWithOverlap() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertFalse(range.isAfter(otherRange));
    }

    @Test
    void testMeetsWithMeetingRanges() {
        TemporalRange range1 = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        TemporalRange range2 = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertTrue(range1.meets(range2));
    }

    @Test
    void testMeetsWithNonMeetingRanges() {
        TemporalRange range1 = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        TemporalRange range2 = new TemporalRange(Instant.parse("2022-02-01T00:00:01Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertFalse(range1.meets(range2));
    }

    @Test
    void testIsMetByWithMeetingRanges() {
        TemporalRange range1 = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        TemporalRange range2 = new TemporalRange(Instant.parse("2021-12-01T00:00:00Z"), Instant.parse("2022-01-01T00:00:00Z"));
        Assertions.assertTrue(range1.isMetBy(range2));
    }

    @Test
    void testIsMetByWithNonMeetingRanges() {
        TemporalRange range1 = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        TemporalRange range2 = new TemporalRange(Instant.parse("2021-11-30T00:00:00Z"), Instant.parse("2021-12-01T00:00:00Z"));
        Assertions.assertFalse(range1.isMetBy(range2));
    }

    @Test
    void testOverlapsBeforeWithOverlappingRangeAtEnd() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertTrue(range.overlapsBefore(otherRange));
    }

    @Test
    void testOverlapsBeforeWithNonOverlappingRangeAfter() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"));
        Assertions.assertFalse(range.overlapsBefore(otherRange));
    }

    @Test
    void testOverlapsBeforeWithNonOverlappingRangeBefore() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Assertions.assertFalse(range.overlapsBefore(otherRange));
    }

    @Test
    void testOverlapsAfterWithOverlappingRangeAtStart() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        Assertions.assertTrue(range.overlapsAfter(otherRange));
    }

    @Test
    void testOverlapsAfterWithNonOverlappingRangeBefore() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-04-01T00:00:00Z"));
        Assertions.assertFalse(range.overlapsAfter(otherRange));
    }

    @Test
    void testOverlapsAfterWithNonOverlappingRangeAfter() {
        TemporalRange range = new TemporalRange(Instant.parse("2022-02-01T00:00:00Z"), Instant.parse("2022-03-01T00:00:00Z"));
        TemporalRange otherRange = new TemporalRange(Instant.parse("2022-01-01T00:00:00Z"), Instant.parse("2022-02-01T00:00:00Z"));
        Assertions.assertFalse(range.overlapsAfter(otherRange));
    }
}
