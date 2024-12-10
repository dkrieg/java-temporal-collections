package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class ImmutableTemporalCollectionTest {

    @Test
    void testGetAsOfNow_presentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);

        TemporalValue<Integer> expectedValue = Mockito.mock(TemporalValue.class);
        when(mutableTemporalCollectionMock.getAsOfNow()).thenReturn(Optional.of(expectedValue));

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getAsOfNow();

        // Assert
        assertEquals(Optional.of(expectedValue), result);
    }

    @Test
    void testGetAsOfNow_absentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);

        when(mutableTemporalCollectionMock.getAsOfNow()).thenReturn(Optional.empty());

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getAsOfNow();

        // Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetAsOf_presentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        TemporalValue<Integer> expectedValue = Mockito.mock(TemporalValue.class);
        Instant testInstant = Instant.now();
        when(mutableTemporalCollectionMock.getAsOf(testInstant)).thenReturn(Optional.of(expectedValue));

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getAsOf(testInstant);

        // Assert
        assertEquals(Optional.of(expectedValue), result);
    }

    @Test
    void testGetAsOf_absentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        Instant testInstant = Instant.now();
        when(mutableTemporalCollectionMock.getAsOf(testInstant)).thenReturn(Optional.empty());

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getAsOf(testInstant);

        // Assert
        assertEquals(Optional.empty(), result);
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetPriorToNow_presentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);

        TemporalValue<Integer> expectedValue = Mockito.mock(TemporalValue.class);
        when(mutableTemporalCollectionMock.getPriorToNow()).thenReturn(Optional.of(expectedValue));

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getPriorToNow();

        // Assert
        assertEquals(Optional.of(expectedValue), result);
    }

    @Test
    void testGetPriorToNow_absentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);

        when(mutableTemporalCollectionMock.getPriorToNow()).thenReturn(Optional.empty());

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getPriorToNow();

        // Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetPriorTo_presentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        TemporalValue<Integer> expectedValue = Mockito.mock(TemporalValue.class);
        Instant testInstant = Instant.now();
        when(mutableTemporalCollectionMock.getPriorTo(testInstant)).thenReturn(Optional.of(expectedValue));

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getPriorTo(testInstant);

        // Assert
        assertEquals(Optional.of(expectedValue), result);
    }

    @Test
    void testGetPriorTo_absentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        Instant testInstant = Instant.now();
        when(mutableTemporalCollectionMock.getPriorTo(testInstant)).thenReturn(Optional.empty());

        // Act
        Optional<TemporalValue<Integer>> result = immutableTemporalCollection.getPriorTo(testInstant);

        // Assert
        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetInRange_presentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        TemporalValue<Integer> expectedValue = Mockito.mock(TemporalValue.class);
        TemporalRange temporalRange = new TemporalRange(Instant.now(), Instant.now().plusSeconds(60));
        Collection<TemporalValue<Integer>> expectedCollection = new ArrayList<>();
        expectedCollection.add(expectedValue);
        when(mutableTemporalCollectionMock.getInRange(temporalRange)).thenReturn(expectedCollection);

        // Act
        Collection<TemporalValue<Integer>> resultCollection = immutableTemporalCollection.getInRange(temporalRange);

        // Assert
        assertEquals(expectedCollection, resultCollection);
    }

    @Test
    void testGetInRange_absentValue_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        TemporalRange temporalRange = new TemporalRange(Instant.now(), Instant.now().plusSeconds(60));
        when(mutableTemporalCollectionMock.getInRange(temporalRange)).thenReturn(new ArrayList<>());

        // Act
        Collection<TemporalValue<Integer>> resultCollection = immutableTemporalCollection.getInRange(temporalRange);

        // Assert
        assertTrue(resultCollection.isEmpty());
    }

    @Test
    void testSize_nonEmptyCollection_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        when(mutableTemporalCollectionMock.size()).thenReturn(5);

        // Act
        int size = immutableTemporalCollection.size();

        // Assert
        assertEquals(5, size);
    }

    @Test
    void testSize_emptyCollection_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        when(mutableTemporalCollectionMock.size()).thenReturn(0);

        // Act
        int size = immutableTemporalCollection.size();

        // Assert
        assertEquals(0, size);
    }

    @Test
    void testIsEmpty_nonEmptyCollection_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        when(mutableTemporalCollectionMock.isEmpty()).thenReturn(false);

        // Act
        boolean isEmpty = immutableTemporalCollection.isEmpty();

        // Assert
        assertFalse(isEmpty);
    }

    @Test
    void testIsEmpty_emptyCollection_case() {
        // Arrange
        MutableTemporalCollection<Integer, TemporalValue<Integer>> mutableTemporalCollectionMock = Mockito.mock(MutableTemporalCollection.class);
        ImmutableTemporalCollection<Integer, TemporalValue<Integer>> immutableTemporalCollection = new ImmutableTemporalCollection<>(mutableTemporalCollectionMock);
        when(mutableTemporalCollectionMock.isEmpty()).thenReturn(true);

        // Act
        boolean isEmpty = immutableTemporalCollection.isEmpty();

        // Assert
        assertTrue(isEmpty);
    }
}
