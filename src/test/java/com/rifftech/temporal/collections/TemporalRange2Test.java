package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TemporalRange2Test {
    @Test
    void finishes_startAfterEndEqual_ReturnsTrue() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertTrue(range1.finishes(range2), "TemporalRange.finishes has returned false, but true was expected");
    }

    @Test
    void finishes_startAfterEndDifferent_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.finishes(range2), "TemporalRange.finishes has returned true, but false was expected");
    }

    @Test
    void finishes_startBeforeEndEqual_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T06:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.finishes(range2), "TemporalRange.finishes has returned true, but false was expected");
    }

    @Test
    void isFinishedBy_startBeforeBothEndsEqual_ReturnsTrue() {
        Instant start = Instant.parse("2024-03-01T06:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertTrue(range1.isFinishedBy(range2), "TemporalRange.isFinishedBy has returned false, but true was expected");
    }

    @Test
    void isFinishedBy_startAndEndDifferent_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T06:00:00Z");
        Instant end = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.isFinishedBy(range2), "TemporalRange.isFinishedBy has returned true, but false was expected");
    }

    @Test
    void isFinishedBy_startAfterBothEndsEqual_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.isFinishedBy(range2), "TemporalRange.isFinishedBy has returned true, but false was expected");
    }

    @Test
    void includes_startBeforeEndsAfter_ReturnsTrue() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T11:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertTrue(range1.includes(range2), "TemporalRange.includes has returned false, but true was expected");
    }

    @Test
    void includes_startDifferentEndsAfter_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T12:00:00Z");
        Instant end = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T11:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.includes(range2), "TemporalRange.includes has returned true, but false was expected");
    }

    @Test
    void includes_startBeforeEndsDifferent_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T11:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.includes(range2), "TemporalRange.includes has returned true, but false was expected");
    }

    @Test
    void isDuring_startAfterEndBefore_ReturnsTrue() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertTrue(range1.isDuring(range2), "TemporalRange.isDuring has returned false, but true was expected");
    }

    @Test
    void isDuring_startBeforeEndBefore_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T07:00:00Z");
        Instant end = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.isDuring(range2), "TemporalRange.isDuring has returned true, but false was expected");
    }

    @Test
    void isDuring_startAfterEndAfter_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.isDuring(range2), "TemporalRange.isDuring has returned true, but false was expected");
    }

    @Test
    void starts_startAfterEndBefore_ReturnsTrue() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start, end2);

        assertTrue(range1.starts(range2), "TemporalRange.starts has returned false, but true was expected");
    }

    @Test
    void starts_startBeforeEndBefore_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T07:00:00Z");
        Instant end = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.starts(range2), "TemporalRange.starts has returned true, but false was expected");
    }

    @Test
    void starts_startAfterEndAfter_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T08:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.starts(range2), "TemporalRange.starts has returned true, but false was expected");
    }

    @Test
    void isStartedBy_endAfterStartsEqual_ReturnsTrue() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant end2 = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range2 = new TemporalRange(start, end2);

        assertTrue(range1.isStartedBy(range2), "TemporalRange.isStartedBy has returned false, but true was expected");
    }

    @Test
    void isStartedBy_endDifferentStartsEqual_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant end2 = Instant.parse("2024-03-01T13:00:00Z");
        TemporalRange range2 = new TemporalRange(start, end2);

        assertFalse(range1.isStartedBy(range2), "TemporalRange.isStartedBy has returned true, but false was expected");
    }

    @Test
    void isStartedBy_endAfterStartsDifferent_ReturnsFalse() {
        Instant start = Instant.parse("2024-03-01T09:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        Instant start2 = Instant.parse("2024-03-01T10:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range2 = new TemporalRange(start2, end2);

        assertFalse(range1.isStartedBy(range2), "TemporalRange.isStartedBy has returned true, but false was expected");
    }

    @Test
    void compareTo_sameStartAndEndInstants_ReturnsZero() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end);

        TemporalRange range2 = new TemporalRange(start, end);

        assertEquals(0, range1.compareTo(range2), "Comparing two temporal ranges with the same start and end instants did not return 0.");
    }

    @Test
    void compareTo_earlierStartInstant_ReturnsNegative() {
        Instant start1 = Instant.parse("2024-03-01T09:00:00Z");
        Instant start2 = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start1, end);

        TemporalRange range2 = new TemporalRange(start2, end);

        assertTrue(range1.compareTo(range2) < 0, "Comparing a temporal range with an earlier start instant to one with a later start instant did not return a negative number.");
    }

    @Test
    void compareTo_laterStartInstant_ReturnsPositive() {
        Instant start1 = Instant.parse("2024-03-01T11:00:00Z");
        Instant start2 = Instant.parse("2024-03-01T10:00:00Z");
        Instant end = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start1, end);

        TemporalRange range2 = new TemporalRange(start2, end);

        assertTrue(range1.compareTo(range2) > 0, "Comparing a temporal range with a later start instant to one with an earlier start instant did not return a positive number.");
    }

    @Test
    void compareTo_earlierEndInstant_ReturnsNegative() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end1 = Instant.parse("2024-03-01T11:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T12:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end1);

        TemporalRange range2 = new TemporalRange(start, end2);

        assertTrue(range1.compareTo(range2) < 0, "Comparing a temporal range with an earlier end instant to one with a later end instant did not return a negative number.");
    }

    @Test
    void compareTo_laterEndInstant_ReturnsPositive() {
        Instant start = Instant.parse("2024-03-01T10:00:00Z");
        Instant end1 = Instant.parse("2024-03-01T12:00:00Z");
        Instant end2 = Instant.parse("2024-03-01T11:00:00Z");
        TemporalRange range1 = new TemporalRange(start, end1);

        TemporalRange range2 = new TemporalRange(start, end2);

        assertTrue(range1.compareTo(range2) > 0, "Comparing a temporal range with a later end instant to one with an earlier end instant did not return a positive number.");
    }
}
