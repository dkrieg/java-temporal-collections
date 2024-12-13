package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractTemporalCollectionTest<V extends TemporalValue<Integer>> extends MutableTemporalCollectionTest<V> {

    @Test
    public void testTemporalValue_validTemporalRange_and_validValue_shouldReturnBusinessTemporalValue() {
        // Arrange
        TemporalRange validTemporalRange = TemporalRange.fromTo(Instant.now(), Instant.now().plusSeconds(10));
        Integer value = 10;

        AbstractTemporalCollection<Integer, V> collection = (AbstractTemporalCollection<Integer, V>) createInstance();

        // Act
        V actualResult = collection.temporalValue(validTemporalRange, value);

        // Assert
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.value()).isEqualTo(value);
    }

    @Test
    public void testTemporalValue_nullTemporalRange_and_validValue_shouldThrowNullPointerException() {
        // Arrange
        TemporalRange validTemporalRange = null;
        Integer value = 10;

        AbstractTemporalCollection<Integer, V> collection = (AbstractTemporalCollection<Integer, V>) createInstance();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> collection.temporalValue(validTemporalRange, value));
    }

    @Test
    public void testTemporalValue_validTemporalRange_and_nullValue_shouldThrowNullPointerException() {
        // Arrange
        TemporalRange validTemporalRange = TemporalRange.fromTo(Instant.now(), Instant.now().plusSeconds(10));
        Integer value = null;

        AbstractTemporalCollection<Integer, V> collection = (AbstractTemporalCollection<Integer, V>) createInstance();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> collection.temporalValue(validTemporalRange, value));
    }
}
