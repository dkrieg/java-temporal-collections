package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiTemporalCollectionTest extends MutableTemporalCollectionTest<BiTemporalValue<Integer>> {

    @Override
    protected MutableTemporalCollection<Integer, BiTemporalValue<Integer>> createInstance() {
        return new BiTemporalCollection<>();
    }

    @Test
    void whenEffectiveAsOfNonExistentItem_thenSizeIncreases() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant instant = Instant.now();
        Integer item = 5;

        int sizeBefore = biTemporalCollection.size();
        biTemporalCollection.effectiveAsOf(instant, item);
        int sizeAfter = biTemporalCollection.size();

        assertTrue(sizeAfter > sizeBefore);
    }

    @Test
    void whenEffectiveAsOfExistentItem_thenSizeSame() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant instant = Instant.now();
        Integer item = 5;

        biTemporalCollection.effectiveAsOf(instant, item);
        int sizeBefore = biTemporalCollection.size();
        biTemporalCollection.effectiveAsOf(instant, item);
        int sizeAfter = biTemporalCollection.size();

        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    void whenEffectiveAsOf_thenNotNull() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant instant = Instant.now();
        Integer item = 5;

        assertThrows(NullPointerException.class, () -> biTemporalCollection.effectiveAsOf(instant, null));
        assertThrows(NullPointerException.class, () -> biTemporalCollection.effectiveAsOf(null, item));
    }

    @Test
    void whenExpireAsOf_thenEffectiveAsOf() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant instant = Instant.now();
        Integer item = 5;

        biTemporalCollection.expireAsOf(instant);
        assertThat(biTemporalCollection.size()).isEqualTo(1);
        biTemporalCollection.effectiveAsOf(instant, item);
        assertThat(biTemporalCollection.size()).isEqualTo(1);
    }

    @Test
    void whenEffectiveAsOf_withTransactionTime_thenGetAsOf_withTransactionTime_isPresent() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant instant = Instant.now().minusSeconds(10);
        Integer item = 5;

        biTemporalCollection.effectiveAsOf(instant, instant, item);
        assertThat(biTemporalCollection.getAsOf(instant, instant)).isPresent().map(TemporalValue::value).contains(5);
    }

    @Test
    void whenEffectiveAsOf_withTransactionTime_thenNotNull() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant instant = Instant.now();
        Integer item = 5;

        assertThrows(NullPointerException.class, () -> biTemporalCollection.effectiveAsOf(instant, instant, null));
        assertThrows(NullPointerException.class, () -> biTemporalCollection.effectiveAsOf(instant, null, item));
        assertThrows(NullPointerException.class, () -> biTemporalCollection.effectiveAsOf(null, instant, item));
    }

    @Test
    void whenGetPriorTo_withTransactionTime() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant now = Instant.now();
        Instant prior1 = now.minusSeconds(10);
        Instant prior2 = prior1.minusSeconds(20);
        Integer item = 5;
        Integer item2 = 10;
        Integer item3 = 15;

        biTemporalCollection.effectiveAsOf(now, now, item);
        biTemporalCollection.effectiveAsOf(prior1, prior1, item2);
        biTemporalCollection.effectiveAsOf(prior1, prior2, item3);
        assertThat(biTemporalCollection.getPriorTo(now, now)).isPresent().map(TemporalValue::value).contains(10);
        assertThat(biTemporalCollection.getPriorTo(now, prior1)).isPresent().map(TemporalValue::value).contains(10);
        assertThat(biTemporalCollection.getPriorTo(now, prior2)).isPresent().map(TemporalValue::value).contains(15);
        assertThat(biTemporalCollection.getPriorTo(prior1, now)).isEmpty();
        assertThat(biTemporalCollection.getPriorTo(prior1, prior1)).isEmpty();
        assertThat(biTemporalCollection.getPriorTo(prior1, prior2)).isEmpty();
        assertThat(biTemporalCollection.getPriorTo(prior2, now)).isEmpty();
        assertThat(biTemporalCollection.getPriorTo(prior2, prior1)).isEmpty();
        assertThat(biTemporalCollection.getPriorTo(prior2, prior2)).isEmpty();
    }
}
