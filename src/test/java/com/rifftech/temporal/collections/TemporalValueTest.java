package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TemporalValueTest {
    @Test
    void compareTo_sameTemporalRange_returnZero() {
        TemporalRange tr1 = nowUntilMax();
        TemporalValue<String> t1 = createTemporalValue(tr1, "Hello");
        TemporalValue<String> t2 = createTemporalValue(tr1, "World");
        assertEquals(0, t1.compareTo(t2));
    }

    @Test
    void compareTo_differentTemporalRange_returnLessThanZero() {
        Instant start1 = Instant.now().minusSeconds(20);
        Instant end1 = Instant.now().minusSeconds(10);
        TemporalValue<String> t1 = createTemporalValue(fromTo(start1, end1), "Hello");
        TemporalValue<String> t2 = createTemporalValue(fromToMax(end1), "World");
        assertTrue(t1.compareTo(t2) < 0);
    }

    @Test
    void compareTo_differentTemporalRange_returnGreaterThanZero() {
        Instant start1 = Instant.now().minusSeconds(20);
        Instant end1 = Instant.now().minusSeconds(10);
        TemporalValue<String> t1 = createTemporalValue(fromTo(start1, end1), "Hello");
        TemporalValue<String> t2 = createTemporalValue(fromToMax(end1), "World");
        assertTrue(t2.compareTo(t1) > 0);
    }

    protected abstract TemporalValue<String> createTemporalValue(TemporalRange temporalRange, String value);
}
