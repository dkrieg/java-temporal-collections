package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class BusinessTemporalValueTest extends TemporalValueTest {

    @Override
    protected TemporalValue<String> createTemporalValue(TemporalRange temporalRange, String value) {
        return new BusinessTemporalValue<>(temporalRange, value);
    }
}
