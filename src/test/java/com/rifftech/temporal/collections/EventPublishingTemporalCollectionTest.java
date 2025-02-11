package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.TemporalEventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

public class EventPublishingTemporalCollectionTest {

    @Test
    public void testEventPublishingTemporalCollection_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);

        assertThatNullPointerException().isThrownBy(() -> new EventPublishingTemporalCollection<>(mockCollection, null));
        assertThatNullPointerException().isThrownBy(() -> new EventPublishingTemporalCollection<>(null, mockEventProducer));
    }

    @Test
    public void effectiveAsOf_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(null, 5));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), null));
    }

    @Test
    public void effectiveAsOfSkipEvent_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOfSkipEvent(null, 5));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOfSkipEvent(Instant.now(), null));
    }

    @Test
    public void expireAsOf_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.expireAsOf(null));
    }

    @Test
    public void expireAsOfSkipEvent_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.expireAsOfSkipEvent(null));
    }

    @Test
    public void getAsOf_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null));
    }

    @Test
    public void getPriorTo_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null));
    }

    @Test
    public void getInRange_WithNull() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null));
    }

    @Test
    public void getAsOf_WithNonNullInput_ReturnsExpectedRecord() {
        // Initialize mock objects
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> testInstance = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        // Initialize expected return record
        Integer expectedValue = 10;
        Instant now = Instant.now();

        TemporalRange validTimeRange = fromToMax(now);
        TemporalRecord<Integer> expectedRecord = new TemporalRecord<>(validTimeRange, expectedValue);

        // Set up mock method call return
        when(mockCollection.getAsOf(now)).thenReturn(Optional.of(expectedRecord));

        // Assertion
        assertThat(testInstance.getAsOf(now)).isPresent().contains(expectedRecord);
    }

    @Test
    public void getAsOf_WithNonNullInput_ReturnsEmptyOptional() {
        // Initialize mock objects
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> testInstance = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        // Set up mock method call return
        Instant now = Instant.now();
        when(mockCollection.getAsOf(now)).thenReturn(Optional.empty());

        // Assertion
        assertThat(testInstance.getAsOf(now)).isEmpty();
    }

    @Test
    public void getPriorTo_WithNonNullInput_ReturnsExpectedRecord() {
        // Initialize mock objects
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> testInstance = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        // Initialize expected return record
        Integer expectedValue = 10;
        Instant now = Instant.now();

        TemporalRange validTimeRange = fromToMax(now);
        TemporalRecord<Integer> expectedRecord = new TemporalRecord<>(validTimeRange, expectedValue);

        // Set up mock method call return
        when(mockCollection.getPriorTo(now)).thenReturn(Optional.of(expectedRecord));

        // Assertion
        assertThat(testInstance.getPriorTo(now)).isPresent().contains(expectedRecord);
    }

    @Test
    public void getPriorTo_WithNonNullInput_ReturnsEmptyOptional() {
        // Initialize mock objects
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> testInstance = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        // Set up mock method call return
        Instant now = Instant.now();
        when(mockCollection.getPriorTo(now)).thenReturn(Optional.empty());

        // Assertion
        assertThat(testInstance.getPriorTo(now)).isEmpty();
    }

    @Test
    public void getInRange_WithNonNullInput_ReturnsExpectedRecord() {
        // Initialize mock objects
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> testInstance = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        // Initialize expected return record
        Integer expectedValue = 10;
        Instant now = Instant.now();

        TemporalRange validTimeRange = TemporalRange.fromToMax(now);
        TemporalRecord<Integer> expectedRecord = new TemporalRecord<>(validTimeRange, expectedValue);
        Collection<TemporalRecord<Integer>> expectedRecords = List.of(expectedRecord);

        // Set up mock method call return
        when(mockCollection.getInRange(validTimeRange)).thenReturn(expectedRecords);

        // Assertion
        assertThat(testInstance.getInRange(validTimeRange)).hasSize(1).contains(expectedRecord);
    }

    @Test
    public void getInRange_WithNonNullInput_ReturnsEmptyCollection() {
        // Initialize mock objects
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> testInstance = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        // Set up mock method call return
        TemporalRange validTimeRange = TemporalRange.fromToMax(Instant.now());
        when(mockCollection.getInRange(validTimeRange)).thenReturn(List.of());

        // Assertion
        assertThat(testInstance.getInRange(validTimeRange)).isEmpty();
    }

    @Test
    public void size_EmptyCollection_ReturnsZero() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.size()).thenReturn(0);

        assertThat(collection.size()).isEqualTo(0);
    }

    @Test
    public void size_NonEmptyCollection_ReturnsCorrectSize() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.size()).thenReturn(5);

        assertThat(collection.size()).isEqualTo(5);
    }

    @Test
    public void isEmpty_EmptyCollection_ReturnsTrue() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.isEmpty()).thenReturn(true);

        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_NonEmptyCollection_ReturnsFalse() {
        ConcurrentSkipListTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListTemporalCollection.class);
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = new EventPublishingTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.isEmpty()).thenReturn(false);

        assertThat(collection.isEmpty()).isFalse();
    }
}
