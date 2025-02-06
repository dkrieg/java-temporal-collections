package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.MAX;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class ConcurrentSkipListBiTemporalCollectionTest {

    @Test
    public void isEmpty_TrueWhenNoItem() {
        ConcurrentSkipListBiTemporalCollection<String> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void isEmpty_FalseWhenItemExists() {
        ConcurrentSkipListBiTemporalCollection<String> collection = new ConcurrentSkipListBiTemporalCollection<>();
        collection.effectiveAsOf(Instant.now(), MAX, "TestItem");
        assertThat(collection.isEmpty()).isFalse();
    }

    @Test
    public void size_IsZeroWhenNoItem() {
        ConcurrentSkipListBiTemporalCollection<String> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThat(collection.size()).isEqualTo(0);
    }

    @Test
    public void size_IsIncreasedWhenItemAdded() {
        ConcurrentSkipListBiTemporalCollection<String> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant now = Instant.now();
        collection.effectiveAsOf(now.minusSeconds(5), now, "TestItem1");
        assertThat(collection.size()).isEqualTo(1);
        collection.effectiveAsOf(now, MAX, "TestItem2");
        assertThat(collection.size()).isEqualTo(2);
    }

    @Test
    public void effectiveAsOfNow_WhenEmpty() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThat(collection.effectiveAsOfNow(1)).isEmpty();
    }

    @Test
    public void effectiveAsOfNow_WhenUsingNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOfNow(null));
    }

    @Test
    public void effectiveAsOf_WhenUsingNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(null, 1));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), null));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), null, 1));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(null, Instant.now(), 1));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), MAX, null));
    }

    @Test
    public void effectiveAsOf_WhenEmpty() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant now = Instant.now();
        assertThat(collection.effectiveAsOf(now, now, 1)).isEmpty();
    }

    @Test
    public void effectiveAsOf_WhenUpdatingExistingEffectiveInstant() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant now = Instant.now();
        assertThat(collection.effectiveAsOf(now, now, 1)).isEmpty();
        assertThat(collection.effectiveAsOf(now, now, 2))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromToMax(now)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
    }

    @Test
    public void effectiveAsOf_WhenUpdatingExistingExpiredInstant() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant now = Instant.now();
        assertThat(collection.effectiveAsOf(now, now, 1)).isEmpty();
        assertThat(collection.effectiveAsOf(now.plusSeconds(5), now.plusSeconds(5), 2))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromTo(now, now.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.systemEffective()).isEqualTo(fromToMax(now)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
        assertThat(collection.effectiveAsOf(now, now, 3))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromTo(now, now.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.systemEffective()).isEqualTo(fromToMax(now)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
        assertThat(collection.effectiveAsOf(now, now.plusSeconds(5), 4))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromTo(now, now.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.systemEffective()).isEqualTo(fromToMax(now)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(3));
    }

    @Test
    public void expireAsOfNow_WhenEmpty() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThat(collection.expireAsOfNow()).isEmpty();
        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void expireAsOf_WhenEmpty() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.expireAsOf(instant)).isEmpty();
        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void expireAsOf_WhenUsingNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.expireAsOf(null));
    }

    @Test
    public void expireAsOf_WhenExpiringExistingEffectiveInstant() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.expireAsOf(instant))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromToMax(instant)))
                .map(BiTemporalRecord::value)
                .hasValue(1);
        assertThat(collection.getAsOf(instant)).isEmpty();
    }

    @Test
    public void expireAsOf_WhenNewerInstantExpiresExistingEffectiveInstant() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.expireAsOf(instant.plusSeconds(5)))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromTo(instant, instant.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
    }

    @Test
    public void expireAsOf_WhenInstantPredatesAnyEffectiveInstant() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.expireAsOf(instant.minusSeconds(5))).isEmpty();
    }

    @Test
    public void expireAsOf_WhenInstantSplitsExpiredAndEffectiveInstant() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant.plusSeconds(5), 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant.minusSeconds(5), 2)).isEmpty();
        assertThat(collection.expireAsOf(instant))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.businessEffective()).isEqualTo(fromTo(instant.minusSeconds(5), instant)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(2));
    }

    @Test
    public void getAsOf_WhenUsingNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null));
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(Instant.now(), null));
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null, Instant.now()));
    }

    @Test
    public void getPriorTo_WhenUsingNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null));
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(Instant.now(), null));
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null, Instant.now()));
    }

    @Test
    public void getAsOfNow_WhenEmpty() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThat(collection.getAsOfNow()).isEmpty();
    }

    @Test
    public void getPriorToNow_WhenEmpty() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThat(collection.getPriorToNow()).isEmpty();
    }

    @Test
    public void getInRange_WhenUsingNull() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null));
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(FOREVER, null));
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null, FOREVER));
    }

    @Test
    public void getInRange_WhenOneExpiredInBusinessTimeOneEffectiveInBusinessTime() {
        ConcurrentSkipListBiTemporalCollection<Integer> collection = new ConcurrentSkipListBiTemporalCollection<>();
        Instant now = Instant.now();
        collection.effectiveAsOf(now, now, 1);
        collection.effectiveAsOf(now, now.plusSeconds(5), 2); // expires previous in system time
        collection.effectiveAsOf(now.plusSeconds(5), now.plusSeconds(5), 3); // expires previous in business time
        assertThat(collection.getInRange(FOREVER)).hasSize(2);
        assertThat(collection.getInRange(FOREVER, FOREVER)).hasSize(3);
    }
}
