package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemporalRange4Test {
    @Test
    public void testFromToNowWithValidInstant() {
        Instant start = Instant.now().minusSeconds(10);
        TemporalRange result = TemporalRange.fromToNow(start);
        assertNotNull(result);
        assertEquals(start, result.start());
        assertTrue(result.end().isAfter(result.start()));
    }

    @Test
    public void testFromToNowWithNullInstant() {
        Instant start = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToNow(start));
    }

    @Test
    public void testFromToNowWithPrecisionWithValidInstantAndPrecision() {
        Instant start = Instant.now().minusSeconds(10);
        ChronoUnit precision = ChronoUnit.SECONDS;
        TemporalRange result = TemporalRange.fromToNow(start, precision);
        assertNotNull(result);
        assertEquals(start.truncatedTo(precision), result.start());
        assertTrue(result.end().isAfter(result.start()));
    }

    @Test
    public void testFromToNowWithPrecisionWithNullInstant() {
        Instant start = null;
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToNow(start, precision));
    }

    @Test
    public void testFromToNowWithPrecisionWithNullPrecision() {
        Instant start = Instant.now();
        ChronoUnit precision = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromToNow(start, precision));
    }

    @Test
    public void testFromToWithValidStartAndEndInstant() {
        Instant start = Instant.now().minusSeconds(20);
        Instant end = Instant.now();
        TemporalRange result = TemporalRange.fromTo(start, end);
        assertNotNull(result);
        assertEquals(start, result.start());
        assertEquals(end, result.end());
    }

    @Test
    public void testFromToWithNullStartInstant() {
        Instant start = null;
        Instant end = Instant.now();
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromTo(start, end));
    }

    @Test
    public void testFromToWithNullEndInstant() {
        Instant start = Instant.now();
        Instant end = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromTo(start, end));
    }

    @Test
    public void testFromToWithStartInstantAfterEndInstant() {
        Instant start = Instant.now();
        Instant end = Instant.now().minusSeconds(20);
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromTo(start, end));
    }

    @Test
    public void testFromToWithPrecisionWithValidInstantsAndPrecision() {
        Instant start = Instant.now().minusSeconds(20);
        Instant end = Instant.now();
        ChronoUnit precision = ChronoUnit.SECONDS;
        TemporalRange result = TemporalRange.fromTo(start, end, precision);
        assertNotNull(result);
        assertEquals(start.truncatedTo(precision), result.start());
        assertEquals(end.truncatedTo(precision), result.end());
    }

    @Test
    public void testFromToWithPrecisionWithNullStartInstant() {
        Instant start = null;
        Instant end = Instant.now();
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromTo(start, end, precision));
    }

    @Test
    public void testFromToWithPrecisionWithNullEndInstant() {
        Instant start = Instant.now();
        Instant end = null;
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromTo(start, end, precision));
    }

    @Test
    public void testFromToWithPrecisionWithNullPrecision() {
        Instant start = Instant.now();
        Instant end = Instant.now();
        ChronoUnit precision = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.fromTo(start, end, precision));
    }

    @Test
    public void testNowForWithValidDuration() {
        Duration duration = Duration.ofSeconds(10);
        TemporalRange result = TemporalRange.nowFor(duration);
        assertNotNull(result);
        assertTrue(Duration.between(result.start(), result.end()).equals(duration));
    }

    @Test
    public void testNowForWithNullDuration() {
        Duration duration = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowFor(duration));
    }

    @Test
    public void testNowForWithNegativeDuration() {
        Duration duration = Duration.ofSeconds(-10);
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowFor(duration));
    }

    @Test
    public void testNowForWithPrecisionWithValidDurationAndPrecision() {
        Duration duration = Duration.ofMinutes(10);
        ChronoUnit precision = ChronoUnit.MINUTES;
        TemporalRange result = TemporalRange.nowFor(duration, precision);
        assertNotNull(result);
        assertTrue(Duration.between(result.start(), result.end()).equals(duration));
        assertEquals(result.start(), result.start().truncatedTo(precision));
        assertEquals(result.end(), result.end().truncatedTo(precision));
    }

    @Test
    public void testNowForWithPrecisionWithNullDuration() {
        Duration duration = null;
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowFor(duration, precision));
    }

    @Test
    public void testNowForWithPrecisionWithNegativeDuration() {
        Duration duration = Duration.ofSeconds(-10);
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowFor(duration, precision));
    }

    @Test
    public void testNowForWithPrecisionWithNullPrecision() {
        Duration duration = Duration.ofMinutes(10);
        ChronoUnit precision = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowFor(duration, precision));
    }

    @Test
    public void testNowUntilMaxDefault() {
        TemporalRange result = TemporalRange.nowUntilMax();
        assertNotNull(result);
        assertTrue(result.start().isBefore(result.end()));
        assertEquals(TemporalRange.MAX, result.end());
    }

    @Test
    public void testNowUntilMaxWithPrecision() {
        ChronoUnit precision = ChronoUnit.SECONDS;
        TemporalRange result = TemporalRange.nowUntilMax(precision);
        assertNotNull(result);
        assertTrue(result.start().isBefore(result.end()));
        assertTrue(Duration.between(result.start(), result.end()).get(precision) >= 0);
        assertEquals(TemporalRange.MAX.truncatedTo(precision), result.end());
    }

    @Test
    public void testNowUntilWithValidEndInstant() {
        Instant end = Instant.now().plusSeconds(20);
        TemporalRange result = TemporalRange.nowUntil(end);
        assertNotNull(result);
        assertTrue(result.start().isBefore(result.end()));
        assertEquals(end, result.end());
    }

    @Test
    public void testNowUntilWithNullEndInstant() {
        Instant end = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowUntil(end));
    }

    @Test
    public void testNowUntilWithEndInstantBeforeNowInstant() {
        Instant end = Instant.now().minusSeconds(20);
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowUntil(end));
    }

    @Test
    public void testNowUntilWithPrecisionWithValidEndInstantAndPrecision() {
        Instant end = Instant.now().plusSeconds(20);
        ChronoUnit precision = ChronoUnit.SECONDS;
        TemporalRange result = TemporalRange.nowUntil(end, precision);
        assertNotNull(result);
        assertEquals(end.truncatedTo(precision), result.end());
        assertTrue(result.start().isBefore(result.end()));
    }

    @Test
    public void testNowUntilWithPrecisionWithNullEndInstant() {
        Instant end = null;
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowUntil(end, precision));
    }

    @Test
    public void testNowUntilWithPrecisionWithEndInstantBeforeNowInstant() {
        Instant end = Instant.now().minusSeconds(20);
        ChronoUnit precision = ChronoUnit.SECONDS;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowUntil(end, precision));
    }

    @Test
    public void testNowUntilWithPrecisionWithNullPrecision() {
        Instant end = Instant.now().plusSeconds(20);
        ChronoUnit precision = null;
        assertThrows(IllegalArgumentException.class, () -> TemporalRange.nowUntil(end, precision));
    }
}
