package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.MAX;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


final class ImmutableTemporalCollectionTest {

    @Test
    void getAsOfNow_returnsDelegateValue() {
        // Arrange
        TemporalRecord<String> record1 = new TemporalRecord<>(new TemporalRange(Instant.now(), MAX), "record");
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getAsOfNow()).thenReturn(Optional.of(record1));
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getAsOfNow();

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(record1);
    }

    @Test
    void getAsOfNow_returnsEmptyWhenNoDelegateValue() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getAsOfNow()).thenReturn(Optional.empty());
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getAsOfNow();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getAsOf_returnsDelegateValue() {
        // Arrange
        Instant now = Instant.now();
        TemporalRecord<String> record1 = new TemporalRecord<>(new TemporalRange(now, MAX), "record");
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getAsOf(now)).thenReturn(Optional.of(record1));
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getAsOf(now);

        // Assert
        assertThat(result).isPresent().hasValue(record1);
    }

    @Test
    void getAsOf_returnsEmptyWhenNoDelegateValue() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getAsOf(Instant.now())).thenReturn(Optional.empty());
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getAsOf(Instant.now());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getAsOf_WhenUsingNull() {
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);
        assertThatNullPointerException().isThrownBy(() -> immutableTemporalCollection.getAsOf(null));
    }

    @Test
    void getPriorTo_WhenUsingNull() {
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);
        assertThatNullPointerException().isThrownBy(() -> immutableTemporalCollection.getPriorTo(null));
    }

    @Test
    void getInRange_WhenUsingNull() {
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);
        assertThatNullPointerException().isThrownBy(() -> immutableTemporalCollection.getInRange(null));
    }

    @Test
    void getPriorToNow_returnsDelegateValue() {
        // Arrange
        TemporalRecord<String> record1 = new TemporalRecord<>(new TemporalRange(Instant.now(), MAX), "record");
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getPriorToNow()).thenReturn(Optional.of(record1));
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getPriorToNow();

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(record1);
    }

    @Test
    void getPriorToNow_returnsEmptyWhenNoDelegateValue() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getPriorToNow()).thenReturn(Optional.empty());
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getPriorToNow();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getPriorTo_returnsDelegateValue() {
        // Arrange
        Instant now = Instant.now();
        TemporalRecord<String> record1 = new TemporalRecord<>(new TemporalRange(now, MAX), "record");
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getPriorTo(now)).thenReturn(Optional.of(record1));
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getPriorTo(now);

        // Assert
        assertThat(result).isPresent();
    }

    @Test
    void getPriorTo_returnsEmptyWhenNoDelegateValue() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getPriorTo(Instant.now())).thenReturn(Optional.empty());
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Optional<TemporalRecord<String>> result = immutableTemporalCollection.getPriorTo(Instant.now());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void getInRange_returnsDelegateValues() {
        // Arrange
        Instant now = Instant.now();
        TemporalRecord<String> record1 = new TemporalRecord<>(new TemporalRange(now.minusSeconds(5), now), "record1");
        TemporalRecord<String> record2 = new TemporalRecord<>(new TemporalRange(now, MAX), "record2");
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getInRange(any())).thenReturn(Arrays.asList(record1, record2));
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Collection<TemporalRecord<String>> result = immutableTemporalCollection.getInRange(FOREVER);

        // Assert
        assertThat(result).contains(record1, record2);
    }

    @Test
    void getInRange_returnsEmptyWhenNoDelegateValues() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.getInRange(any())).thenReturn(Collections.emptyList());
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        Collection<TemporalRecord<String>> result = immutableTemporalCollection.getInRange(FOREVER);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void size_returnsDelegateSize() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.size()).thenReturn(10);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        int result = immutableTemporalCollection.size();

        // Assert
        assertThat(result).isEqualTo(10);
    }

    @Test
    void size_returnsZeroWhenDelegateIsEmpty() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.size()).thenReturn(0);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        int result = immutableTemporalCollection.size();

        // Assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    void isEmpty_returnFalseWhenDelegateIsNotEmpty() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.isEmpty()).thenReturn(false);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        boolean result = immutableTemporalCollection.isEmpty();

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    void isEmpty_returnsTrueWhenDelegateIsEmpty() {
        // Arrange
        TemporalCollection<String> delegate = Mockito.mock(TemporalCollection.class);
        when(delegate.isEmpty()).thenReturn(true);
        ImmutableTemporalCollection<String> immutableTemporalCollection = new ImmutableTemporalCollection<>(delegate);

        // Act
        boolean result = immutableTemporalCollection.isEmpty();

        // Assert
        assertThat(result).isTrue();
    }
}
