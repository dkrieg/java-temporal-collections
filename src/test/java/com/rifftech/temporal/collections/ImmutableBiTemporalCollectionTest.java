package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.nowUntilMax;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ImmutableBiTemporalCollectionTest {

    @Test
    public void testGetAsOfNow() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getAsOfNow()).thenReturn(Optional.of(record));

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getAsOfNow();
        assertEquals(Optional.of(record), result);
    }

    @Test
    public void testGetAsOfNowEmpty() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getAsOfNow()).thenReturn(Optional.empty());

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getAsOfNow();
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetAsOf() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getAsOf(range.start())).thenReturn(Optional.of(record));

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getAsOf(record.businessEffective().start());
        assertEquals(Optional.of(record), result);
    }

    @Test
    public void testGetAsOfEmpty() {
        Instant validTime = Instant.now();
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getAsOf(validTime)).thenReturn(Optional.empty());

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getAsOf(validTime);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetAsOfValidAndTransactionTime() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getAsOf(record.businessEffective().start(), record.systemEffective().start())).thenReturn(Optional.of(record));

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getAsOf(record.businessEffective().start(), record.systemEffective().start());
        assertEquals(Optional.of(record), result);
    }

    @Test
    public void testGetAsOfValidAndTransactionTimeEmpty() {
        Instant validTime = Instant.now();
        Instant transactionTime = Instant.now();
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getAsOf(validTime, transactionTime)).thenReturn(Optional.empty());

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getAsOf(validTime, transactionTime);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetPriorToNowEmpty() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getPriorToNow()).thenReturn(Optional.empty());

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getPriorToNow();
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetPriorToNow() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getPriorToNow()).thenReturn(Optional.of(record));

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getPriorToNow();
        assertEquals(Optional.of(record), result);
    }


    @Test
    public void testGetPriorToSpecificInstantEmpty() {
        Instant validTime = Instant.now();
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getPriorTo(validTime)).thenReturn(Optional.empty());

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getPriorTo(validTime);
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetPriorToSpecificInstant() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getPriorTo(record.businessEffective().start())).thenReturn(Optional.of(record));

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getPriorTo(record.businessEffective().start());
        assertEquals(Optional.of(record), result);
    }


    @Test
    public void testGetPriorToValidAndTransactionTimeEmpty() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getPriorTo(record.businessEffective().start(), record.systemEffective().start())).thenReturn(Optional.empty());

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getPriorTo(record.businessEffective().start(), record.systemEffective().start());
        assertEquals(Optional.empty(), result);
    }

    @Test
    public void testGetPriorToValidAndTransactionTime() {
        TemporalRange range = nowUntilMax();
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.getPriorTo(record.businessEffective().start(), record.systemEffective().start())).thenReturn(Optional.of(record));

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Optional<BiTemporalRecord<String>> result = collection.getPriorTo(record.businessEffective().start(), record.systemEffective().start());
        assertEquals(Optional.of(record), result);
    }

    @Test
    public void testGetInRange() {
        TemporalRange range = new TemporalRange(Instant.now().minus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(1)));
        BiTemporalRecord<String> record = new BiTemporalRecord<>(range, range, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ArrayList<BiTemporalRecord<String>> expectedRecords = new ArrayList<>();
        expectedRecords.add(record);
        when(delegate.getInRange(range)).thenReturn(expectedRecords);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Collection<BiTemporalRecord<String>> result = collection.getInRange(range);
        assertEquals(expectedRecords, new ArrayList<>(result));
    }

    @Test
    public void testGetInRangeEmpty() {
        TemporalRange range = new TemporalRange(Instant.now().minus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(1)));
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ArrayList<BiTemporalRecord<String>> expectedRecords = new ArrayList<>();
        when(delegate.getInRange(range)).thenReturn(expectedRecords);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Collection<BiTemporalRecord<String>> result = collection.getInRange(range);
        assertEquals(expectedRecords, new ArrayList<>(result));
    }

    @Test
    public void testGetInRangeWithTransactionRange() {
        TemporalRange validRange = new TemporalRange(Instant.now().minus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(1)));
        TemporalRange transactionRange = new TemporalRange(Instant.now(), Instant.now().plus(Duration.ofHours(1)));
        BiTemporalRecord<String> record = new BiTemporalRecord<>(validRange, transactionRange, "test");
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ArrayList<BiTemporalRecord<String>> expectedRecords = new ArrayList<>();
        expectedRecords.add(record);
        when(delegate.getInRange(validRange, transactionRange)).thenReturn(expectedRecords);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Collection<BiTemporalRecord<String>> result = collection.getInRange(validRange, transactionRange);
        assertEquals(expectedRecords, new ArrayList<>(result));
    }

    @Test
    public void testGetInRangeWithTransactionRangeEmpty() {
        TemporalRange validRange = new TemporalRange(Instant.now().minus(Duration.ofDays(1)), Instant.now().plus(Duration.ofDays(1)));
        TemporalRange transactionRange = new TemporalRange(Instant.now(), Instant.now().plus(Duration.ofHours(1)));
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ArrayList<BiTemporalRecord<String>> expectedRecords = new ArrayList<>();
        when(delegate.getInRange(validRange, transactionRange)).thenReturn(expectedRecords);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        Collection<BiTemporalRecord<String>> result = collection.getInRange(validRange, transactionRange);
        assertEquals(expectedRecords, new ArrayList<>(result));
    }

    @Test
    public void testCollectionSizeIsZero() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.size()).thenReturn(0);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        int size = collection.size();
        assertEquals(0, size);
    }

    @Test
    public void testCollectionSizeIsNonZero() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.size()).thenReturn(5);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        int size = collection.size();
        assertEquals(5, size);
    }

    @Test
    public void testCollectionIsEmpty() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.isEmpty()).thenReturn(true);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        boolean isEmpty = collection.isEmpty();
        assertEquals(true, isEmpty);
    }

    @Test
    public void testCollectionIsNotEmpty() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        when(delegate.isEmpty()).thenReturn(false);

        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        boolean isEmpty = collection.isEmpty();
        assertEquals(false, isEmpty);
    }

    @Test
    public void getAsOf_WhenUsingNull() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null));
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(Instant.now(), null));
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null, Instant.now()));
    }

    @Test
    public void getPriorTo_WhenUsingNull() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null));
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(Instant.now(), null));
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null, Instant.now()));
    }

    @Test
    public void getInRange_WhenUsingNull() {
        BiTemporalCollection<String> delegate = Mockito.mock(BiTemporalCollection.class);
        ImmutableBiTemporalCollection<String> collection = new ImmutableBiTemporalCollection<>(delegate);
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null));
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(FOREVER, null));
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null, FOREVER));
    }
}
