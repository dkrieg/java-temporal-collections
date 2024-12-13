package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class MutableTemporalCollectionTest<V extends TemporalValue<Integer>> {
    protected abstract MutableTemporalCollection<Integer, V> createInstance();

    @Test
    public void testGetInRange_case1() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant start = Instant.parse("2022-12-01T00:00:00Z");
        Instant end = Instant.parse("2022-12-01T23:59:59Z");
        TemporalRange temporalRange = TemporalRange.fromTo(start, end);

        collection.effectiveAsOf(Instant.parse("2022-12-01T05:00:00Z"), 5);
        collection.effectiveAsOf(Instant.parse("2022-12-01T10:00:00Z"), 10);
        collection.effectiveAsOf(Instant.parse("2022-12-01T15:00:00Z"), 15);

        List<Integer> values = collection.getInRange(temporalRange)
                .stream()
                .map(TemporalValue::value)
                .toList();
        assertThat(values).containsExactly(5, 10, 15);
    }

    @Test
    public void testGetInRange_case2() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant start = Instant.parse("2022-12-02T00:00:00Z");
        Instant end = Instant.parse("2022-12-02T23:59:59Z");
        TemporalRange temporalRange = TemporalRange.fromTo(start, end);

        collection.effectiveAsOf(start.plusSeconds(5), 5);
        collection.effectiveAsOf(start.plusSeconds(10), 10);
        collection.effectiveAsOf(start.plusSeconds(15), 15);

        List<Integer> values = collection.getInRange(temporalRange)
                .stream()
                .map(TemporalValue::value)
                .toList();
        assertThat(values).containsExactly(5, 10, 15);
    }

    @Test
    public void testGetInRange_case3_emptyRange() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant start = Instant.parse("2022-12-03T00:00:00Z");
        Instant end = Instant.parse("2022-12-03T23:59:59Z");
        TemporalRange temporalRange = TemporalRange.fromTo(start, end);

        assertThat(collection.getInRange(temporalRange)).isEmpty();
    }

    @Test
    public void testIsEmpty_whenTemporalCollectionIsNonEmpty() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(instant, 5);

        assertThat(collection.isEmpty()).isFalse();
    }

    @Test
    public void testIsEmpty_whenTemporalCollectionIsEmpty() {
        MutableTemporalCollection<Integer, V> collection = createInstance();

        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void testSize_whenTemporalCollectionIsNonEmpty() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instantOne = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(instantOne, 5);

        Instant instantTwo = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(instantTwo, 10);

        Instant instantThree = Instant.parse("2022-12-03T00:00:00Z");
        collection.effectiveAsOf(instantThree, 15);

        assertThat(collection.size()).isEqualTo(3);
    }

    @Test
    public void testSize_whenTemporalCollectionIsEmpty() {
        MutableTemporalCollection<Integer, V> collection = createInstance();

        assertThat(collection.size()).isZero();
    }

    @Test
    public void testGetPriorTo_whenExistPrior() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant firstInstant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(secondInstant, 20);

        Instant verifyInstant = Instant.parse("2022-12-02T00:00:01Z");
        Optional<V> optional = collection.getPriorTo(verifyInstant);
        assertThat(optional).isPresent().map(TemporalValue::value).contains(10);
    }

    @Test
    public void testGetPriorTo_whenNoExistPrior() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant firstInstant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(secondInstant, 20);

        Instant verifyInstant = Instant.parse("2022-11-30T00:00:01Z");
        Optional<V> optional = collection.getPriorTo(verifyInstant);
        assertThat(optional).isEmpty();
    }

    @Test
    public void testGetPriorToNow_whenExistPrior() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant firstInstant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(secondInstant, 20);

        Optional<V> optional = collection.getPriorToNow();
        assertThat(optional).isPresent().map(TemporalValue::value).contains(10);
    }

    @Test
    public void testGetPriorToNow_whenNoExistPrior() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant now = Instant.now();
        Instant firstInstant = now.plusSeconds(10);
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = now.plusSeconds(20);
        collection.effectiveAsOf(secondInstant, 20);

        Optional<V> optional = collection.getPriorToNow();
        assertThat(optional).isEmpty();
    }

    @Test
    public void testGetAsOfWhenExist() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant firstInstant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(secondInstant, 20);

        Instant verifyInstant = Instant.parse("2022-12-01T00:01:00Z");
        Optional<V> optional = collection.getAsOf(verifyInstant);
        assertThat(optional).isPresent().map(TemporalValue::value).contains(10);
    }

    @Test
    public void testGetAsOfWhenDoesNotExist() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant firstInstant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(secondInstant, 20);

        Instant verifyInstant = Instant.parse("2022-12-03T00:01:00Z");
        Optional<V> optional = collection.getAsOf(verifyInstant);
        assertThat(optional).isPresent().map(TemporalValue::value).contains(20);
    }

    @Test
    public void testGetAsOfWhenEmpty() {
        MutableTemporalCollection<Integer, V> collection = createInstance();

        Instant verifyInstant = Instant.parse("2022-12-01T00:01:00Z");
        Optional<V> optional = collection.getAsOf(verifyInstant);
        assertThat(optional).isEmpty();
    }

    @Test
    public void testGetAsOfNow_whenExist() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant firstInstant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = Instant.parse("2022-12-02T00:00:00Z");
        collection.effectiveAsOf(secondInstant, 20);

        Optional<V> optional = collection.getAsOfNow();
        assertThat(optional).isPresent().map(TemporalValue::value).contains(20);
    }

    @Test
    public void testGetAsOfNow_whenDoesNotExist() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant now = Instant.now();
        Instant firstInstant = now.plusSeconds(10);
        collection.effectiveAsOf(firstInstant, 10);

        Instant secondInstant = now.plusSeconds(20);
        collection.effectiveAsOf(secondInstant, 20);

        Optional<V> optional = collection.getAsOfNow();
        assertThat(optional).isEmpty();
    }

    @Test
    public void testGetAsOfNow_whenEmpty() {
        MutableTemporalCollection<Integer, V> collection = createInstance();

        Optional<V> optional = collection.getAsOfNow();
        assertThat(optional).isEmpty();
    }

    @Test
    public void testExpireAsOf_withExistingValue() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instant = Instant.parse("2022-12-01T00:00:00Z");
        collection.effectiveAsOf(instant, 10);

        collection.expireAsOf(instant);
        Optional<V> optional = collection.getAsOf(instant);

        assertThat(optional).isNotPresent();
    }

    @Test
    public void testExpireAsOf_withNonExistingValue() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instant = Instant.parse("2022-12-01T00:00:00Z");

        collection.expireAsOf(instant);
        Optional<V> optional = collection.getAsOf(instant);

        assertThat(optional).isNotPresent();
    }

    @Test
    public void testExpireAsOf_withNullOrInvalidParameter() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instant = null;
        assertThrows(NullPointerException.class, () -> collection.expireAsOf(instant));
    }

    @Test
    public void testEffectiveAsOf_withNullOrInvalidParameter() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instant = null;
        Integer value = null;

        assertThrows(NullPointerException.class, () -> collection.effectiveAsOf(instant, 5));
        assertThrows(NullPointerException.class, () -> collection.effectiveAsOf(Instant.now(), value));
    }

    @Test
    public void testEffectiveAsOf_withValidParameter() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant instant = Instant.now();
        Integer value = 10;

        collection.effectiveAsOf(instant, value);

        Optional<V> optional = collection.getAsOfNow();
        assertThat(optional).isPresent().map(TemporalValue::value).contains(value);
    }

    @Test
    public void testExpireAsOfNow_whenExist() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Instant now = Instant.now();
        Instant instantOne = now.minusSeconds(1);
        collection.effectiveAsOf(instantOne, 10);

        Instant instantTwo = now.plusSeconds(1);
        collection.effectiveAsOf(instantTwo, 20);

        collection.expireAsOfNow();
        Optional<V> optional = collection.getAsOfNow();

        assertThat(optional).isEmpty();
        assertThat(collection.size()).isEqualTo(3);
    }

    @Test
    public void testExpireAsOfNow_whenNotExist() {
        MutableTemporalCollection<Integer, V> collection = createInstance();

        collection.expireAsOfNow();
        Optional<V> optional = collection.getAsOfNow();

        assertThat(optional).isEmpty();
        assertThat(collection.size()).isEqualTo(1);
    }

    @Test
    public void testEffectiveAsOfNow_withExistentValue() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        collection.effectiveAsOf(Instant.parse("2022-12-01T00:00:00Z"), 10);

        collection.effectiveAsOfNow(20);
        Optional<V> optional = collection.getAsOfNow();
        assertThat(optional).isPresent().map(TemporalValue::value).contains(20);
    }

    @Test
    public void testEffectiveAsOfNow_withValidParameter() {
        MutableTemporalCollection<Integer, V> collection = createInstance();
        Integer value = null;

        assertThrows(NullPointerException.class, () -> collection.effectiveAsOfNow(value));
    }
}
