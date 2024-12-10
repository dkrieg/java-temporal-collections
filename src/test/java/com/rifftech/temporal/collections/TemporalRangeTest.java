package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TemporalRangeTest {

    @Test
    public void validateInstants_withValidInstants_noExceptionThrown() {
        Instant start = Instant.now();
        Instant end = start.plusSeconds(1);

        TemporalRange.validateInstants(start, end);
    }

    @Test
    public void validateInstants_withNullStart_throwsException() {
        Instant end = Instant.now();

        assertThrows(IllegalArgumentException.class, () -> TemporalRange.validateInstants(null, end));
    }

    @Test
    public void validateInstants_withNullEnd_throwsException() {
        Instant start = Instant.now();

        assertThrows(IllegalArgumentException.class, () -> TemporalRange.validateInstants(start, null));
    }

    @Test
    public void validateInstants_withEndBeforeStart_throwsException() {
        Instant start = Instant.now();
        Instant end = start.minusSeconds(1);

        assertThrows(IllegalArgumentException.class, () -> TemporalRange.validateInstants(start, end));
    }

    @Test
    public void validateInstants_withStartEqualsEnd_throwsException() {
        Instant start = Instant.now();

        assertThrows(IllegalArgumentException.class, () -> TemporalRange.validateInstants(start, start));
    }

    @Test
    public void validateInstants_sameInstantDifferentOffsets_throwsException() {
        Instant start = Instant.now();
        Instant end = start.atZone(ZoneOffset.UTC).plusHours(2).toInstant();

        TemporalRange.validateInstants(start, end);
    }
}