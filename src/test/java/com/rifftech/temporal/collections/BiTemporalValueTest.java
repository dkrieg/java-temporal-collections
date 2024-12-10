package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiTemporalValueTest {
    @Test
    void compareTo_sameTemporalRange_returnZero() {
        TemporalRange tr1 = nowUntilMax();
        BiTemporalValue<String> t1 = createBiTemporalValue(tr1, tr1, "Hello");
        BiTemporalValue<String> t2 = createBiTemporalValue(tr1, tr1, "World");
        assertEquals(0, t1.compareTo(t2));
    }

    @Test
    void compareTo_differentTemporalRange_returnLessThanZero() {
        TemporalRange transactionRange = nowUntilMax();
        Instant start1 = Instant.now().minusSeconds(20);
        Instant end1 = Instant.now().minusSeconds(10);
        BiTemporalValue<String> t1 = createBiTemporalValue(fromTo(start1, end1), transactionRange, "Hello");
        BiTemporalValue<String> t2 = createBiTemporalValue(fromToMax(end1), transactionRange, "World");
        assertTrue(t1.compareTo(t2) < 0);
    }

    @Test
    void compareTo_differentTemporalRange_returnGreaterThanZero() {
        TemporalRange transactionRange = nowUntilMax();
        Instant start1 = Instant.now().minusSeconds(20);
        Instant end1 = Instant.now().minusSeconds(10);
        BiTemporalValue<String> t1 = createBiTemporalValue(fromTo(start1, end1), transactionRange, "Hello");
        BiTemporalValue<String> t2 = createBiTemporalValue(fromToMax(end1), transactionRange, "World");
        assertTrue(t2.compareTo(t1) > 0);
    }

    protected BiTemporalValue<String> createBiTemporalValue(TemporalRange validTemporalRange, TemporalRange transactionTemporalRange, String value) {
        return new BiTemporalValue<>(validTemporalRange, transactionTemporalRange, value);
    }
}
