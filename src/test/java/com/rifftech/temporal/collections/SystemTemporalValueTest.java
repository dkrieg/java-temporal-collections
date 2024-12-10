package com.rifftech.temporal.collections;

class SystemTemporalValueTest extends TemporalValueTest {

    @Override
    protected TemporalValue<String> createTemporalValue(TemporalRange temporalRange, String value) {
        return new SystemTemporalValue<>(temporalRange, value);
    }
}
