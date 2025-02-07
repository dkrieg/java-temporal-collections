package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;


public class ConcurrentSkipListTemporalCollectionTest {

    @Test
    public void effectiveAsOfNow_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThat(collection.effectiveAsOfNow(1)).isEmpty();
    }

    @Test
    public void effectiveAsOf_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
    }

    @Test
    public void effectiveAsOf_WhenUpdatingExistingEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant, 2))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromToMax(instant)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
    }

    @Test
    public void effectiveAsOf_WhenUpdatingExistingExpiredInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant.plusSeconds(5), 2))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromTo(instant, instant.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
        assertThat(collection.effectiveAsOf(instant, 3))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromTo(instant, instant.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
    }

    @Test
    public void effectiveAsOf_WhenNewerInstantExpiresExistingEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant.plusSeconds(5), 2))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromTo(instant, instant.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
    }

    @Test
    public void effectiveAsOf_WhenInstantPredatesAnyEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant.minusSeconds(5), 2)).isEmpty();
    }

    @Test
    public void effectiveAsOf_WhenInstantSplitsExpiredAndEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant.plusSeconds(5), 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant.minusSeconds(5), 2)).isEmpty();
        assertThat(collection.effectiveAsOf(instant, 3))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromTo(instant.minusSeconds(5), instant)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(2));
    }

    @Test
    public void effectiveAsOf_WhenUsingNull() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(null, 1));
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOf(Instant.now(), null));
    }

    @Test
    public void effectiveAsOfNow_WhenUsingNull() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.effectiveAsOfNow(null));
    }

    @Test
    public void expireAsOfNow_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThat(collection.expireAsOfNow()).isEmpty();
        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void expireAsOf_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.expireAsOf(instant)).isEmpty();
        assertThat(collection.isEmpty()).isTrue();
    }

    @Test
    public void expireAsOf_WhenExpiringExistingEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.expireAsOf(instant))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromToMax(instant)))
                .map(TemporalRecord::value)
                .hasValue(1);
    }

    @Test
    public void expireAsOf_WhenNewerInstantExpiresExistingEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.expireAsOf(instant.plusSeconds(5)))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromTo(instant, instant.plusSeconds(5))))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(1));
    }

    @Test
    public void expireAsOf_WhenInstantPredatesAnyEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant, 1)).isEmpty();
        assertThat(collection.expireAsOf(instant.minusSeconds(5))).isEmpty();
    }

    @Test
    public void expireAsOf_WhenInstantSplitsExpiredAndEffectiveInstant() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant instant = Instant.now();
        assertThat(collection.effectiveAsOf(instant.plusSeconds(5), 1)).isEmpty();
        assertThat(collection.effectiveAsOf(instant.minusSeconds(5), 2)).isEmpty();
        assertThat(collection.expireAsOf(instant))
                .isNotEmpty()
                .hasValueSatisfying(record -> assertThat(record.validRange()).isEqualTo(fromTo(instant.minusSeconds(5), instant)))
                .hasValueSatisfying(record -> assertThat(record.value()).isEqualTo(2));
    }

    @Test
    public void expireAsOf_WhenUsingNull() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.expireAsOf(null));
    }

    @Test
    public void getAsOfNow_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThat(collection.getAsOfNow()).isEmpty();
    }

    @Test
    public void getPriorToNow_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThat(collection.getPriorToNow()).isEmpty();
    }

    @Test
    public void getAsOf_WhenUsingNull() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.getAsOf(null));
    }

    @Test
    public void getPriorTo_WhenUsingNull() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.getPriorTo(null));
    }

    @Test
    public void size_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThat(collection.size()).isZero();
    }

    @Test
    public void getInRange_WhenUsingNull() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThatNullPointerException().isThrownBy(() -> collection.getInRange(null));
    }

    @Test
    public void getInRange_WhenEmpty() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        assertThat(collection.getInRange(FOREVER)).isEmpty();
    }

    @Test
    public void getInRange_WhenTemporalRecordsAreContiguous() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant now = Instant.now();
        collection.effectiveAsOf(now.minusSeconds(10), 1);
        collection.effectiveAsOf(now.minusSeconds(5), 2);
        collection.effectiveAsOf(now, 3);
        collection.effectiveAsOf(now.plusSeconds(5), 4);
        collection.effectiveAsOf(now.plusSeconds(10), 5);
        assertThat(collection.getInRange(FOREVER))
                .hasSize(5)
                .isUnmodifiable()
                .satisfiesExactly(record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now.minusSeconds(10), now.minusSeconds(5)));
                    assertThat(record.value()).isEqualTo(1);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now.minusSeconds(5), now));
                    assertThat(record.value()).isEqualTo(2);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now, now.plusSeconds(5)));
                    assertThat(record.value()).isEqualTo(3);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now.plusSeconds(5), now.plusSeconds(10)));
                    assertThat(record.value()).isEqualTo(4);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromToMax(now.plusSeconds(10)));
                    assertThat(record.value()).isEqualTo(5);
                });
    }

    @Test
    public void getInRange_WhenTemporalRecordsHaveExpiredEntry() {
        ConcurrentSkipListTemporalCollection<Integer> collection = new ConcurrentSkipListTemporalCollection<>();
        Instant now = Instant.now();
        collection.effectiveAsOf(now.minusSeconds(10), 1);
        collection.effectiveAsOf(now.minusSeconds(5), 2);
        collection.expireAsOf(now);
        collection.effectiveAsOf(now.plusSeconds(5), 4);
        collection.effectiveAsOf(now.plusSeconds(10), 5);
        assertThat(collection.size()).isEqualTo(5);
        assertThat(collection.getInRange(FOREVER))
                .hasSize(4)
                .isUnmodifiable()
                .satisfiesExactly(record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now.minusSeconds(10), now.minusSeconds(5)));
                    assertThat(record.value()).isEqualTo(1);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now.minusSeconds(5), now));
                    assertThat(record.value()).isEqualTo(2);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromTo(now.plusSeconds(5), now.plusSeconds(10)));
                    assertThat(record.value()).isEqualTo(4);
                }, record -> {
                    assertThat(record.validRange()).isEqualTo(fromToMax(now.plusSeconds(10)));
                    assertThat(record.value()).isEqualTo(5);
                });
    }
}
