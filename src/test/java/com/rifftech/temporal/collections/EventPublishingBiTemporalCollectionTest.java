package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.BiTemporalEventProducer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

public class EventPublishingBiTemporalCollectionTest {

    @Test
    public void EventPublishingBiTemporalCollection_WithNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);

        assertThatNullPointerException().isThrownBy(() -> new EventPublishingBiTemporalCollection<>(mockCollection, null));
        assertThatNullPointerException().isThrownBy(() -> new EventPublishingBiTemporalCollection<>(null, mockEventProducer));
    }

    @Test
    public void effectiveAsOf_WithNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), null, 5));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(null, Instant.now(), 5));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), Instant.now(), null));
    }

    @Test
    public void expireAsOf_WithNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.expireAsOf(null));
    }

    @Test
    public void getAsOf_WithNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null, Instant.now()));
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(Instant.now(), null));
    }

    @Test
    public void getPriorTo_WithNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(Instant.now(), null));
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null, Instant.now()));
    }

    @Test
    public void getInRange_WithNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(FOREVER, null));
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null, FOREVER));
    }

    @Test
    public void size_EmptyCollection_ReturnsZero() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.size()).thenReturn(0);

        assertThat(collection.size()).isEqualTo(0);
    }

    @Test
    public void size_NonEmptyCollection_ReturnsCorrectSize() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.size()).thenReturn(5);

        assertThat(collection.size()).isEqualTo(5);
    }

    @Test
    public void isEmpty_EmptyCollection_ReturnsTrue() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.isEmpty()).thenReturn(true);

        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_NonEmptyCollection_ReturnsFalse() {
        ConcurrentSkipListBiTemporalCollection<Integer> mockCollection = Mockito.mock(ConcurrentSkipListBiTemporalCollection.class);
        BiTemporalEventProducer<Integer> mockEventProducer = Mockito.mock(BiTemporalEventProducer.class);
        EventPublishingBiTemporalCollection<Integer> collection = new EventPublishingBiTemporalCollection<>(mockCollection, mockEventProducer);

        when(mockCollection.isEmpty()).thenReturn(false);

        assertThat(collection.isEmpty()).isFalse();
    }
}
