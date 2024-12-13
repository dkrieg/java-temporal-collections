package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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

        assertThat(biTemporalCollection.getPriorToAsOf(now, now)).isPresent().map(TemporalValue::value).contains(10);
        assertThat(biTemporalCollection.getPriorToAsOf(now, prior1)).isPresent().map(TemporalValue::value).contains(10);
        assertThat(biTemporalCollection.getPriorToAsOf(now, prior2)).isPresent().map(TemporalValue::value).contains(15);
        assertThat(biTemporalCollection.getPriorToAsOf(prior1, now)).isEmpty();
        assertThat(biTemporalCollection.getPriorToAsOf(prior1, prior1)).isEmpty();
        assertThat(biTemporalCollection.getPriorToAsOf(prior1, prior2)).isEmpty();
        assertThat(biTemporalCollection.getPriorToAsOf(prior2, now)).isEmpty();
        assertThat(biTemporalCollection.getPriorToAsOf(prior2, prior1)).isEmpty();
        assertThat(biTemporalCollection.getPriorToAsOf(prior2, prior2)).isEmpty();
    }

    @Test
    void whenGetPriorToPriorTo_thenReturnedItemPrior() {
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
        assertThat(biTemporalCollection.getPriorToPriorTo(now, now)).isPresent().map(TemporalValue::value).contains(15);
        assertThat(biTemporalCollection.getPriorToPriorTo(now, prior1)).isPresent().map(TemporalValue::value).contains(15);
        assertThat(biTemporalCollection.getPriorToPriorTo(now, prior2)).isEmpty();
        assertThat(biTemporalCollection.getPriorToPriorTo(prior1, now)).isEmpty();
        assertThat(biTemporalCollection.getPriorToPriorTo(prior1, prior1)).isEmpty();
        assertThat(biTemporalCollection.getPriorToPriorTo(prior1, prior2)).isEmpty();
    }

    @Test
    void whenGetInRange_withValidTimeRangeAndTransactionTimeRange_thenReturnBiTemporalValuesWithinBothRanges() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant now = Instant.now();
        TemporalRange validTimeRange = new TemporalRange(now.minusSeconds(20), now.plusSeconds(20));
        TemporalRange transactionTimeRange = new TemporalRange(now.minusSeconds(5), now.plusSeconds(5));
        Integer item1 = 1;
        Integer item2 = 2;

        // Add items with matching validTime and transactionTime
        biTemporalCollection.effectiveAsOf(now.minusSeconds(10), now.minusSeconds(3), item1);
        biTemporalCollection.effectiveAsOf(now.plusSeconds(10), now.plusSeconds(3), item2);

        // Check that only items within both the ranges are returned
        Collection<BiTemporalValue<Integer>> rangeItems = biTemporalCollection.getInRange(validTimeRange, transactionTimeRange);
        assertThat(rangeItems.size()).isEqualTo(2);
        assertThat(rangeItems.stream().map(BiTemporalValue::value)).contains(item1, item2);

        rangeItems = biTemporalCollection.getInRangeAsOf(validTimeRange, transactionTimeRange);
        assertThat(rangeItems.size()).isEqualTo(2);
        assertThat(rangeItems.stream().map(BiTemporalValue::value)).contains(item1, item2);
    }

    @Test
    public void testGetInRangeAsOfNow_case1() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant start = Instant.parse("2022-12-01T00:00:00Z");
        Instant end = Instant.parse("2022-12-01T23:59:59Z");
        TemporalRange temporalRange = TemporalRange.fromTo(start, end);

        biTemporalCollection.effectiveAsOf(Instant.parse("2022-12-01T05:00:00Z"), 5);
        biTemporalCollection.effectiveAsOf(Instant.parse("2022-12-01T10:00:00Z"), 10);
        biTemporalCollection.effectiveAsOf(Instant.parse("2022-12-01T15:00:00Z"), 15);

        List<Integer> values = biTemporalCollection.getInRangeAsOfNow(temporalRange)
                .stream()
                .map(TemporalValue::value)
                .toList();
        assertThat(values).containsExactly(5, 10, 15);
    }

    @Test
    public void testGetInRangeAsOfNow_case2() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant start = Instant.parse("2022-12-02T00:00:00Z");
        Instant end = Instant.parse("2022-12-02T23:59:59Z");
        TemporalRange temporalRange = TemporalRange.fromTo(start, end);

        biTemporalCollection.effectiveAsOf(start.plusSeconds(5), 5);
        biTemporalCollection.effectiveAsOf(start.plusSeconds(10), 10);
        biTemporalCollection.effectiveAsOf(start.plusSeconds(15), 15);

        List<Integer> values = biTemporalCollection.getInRangeAsOfNow(temporalRange)
                .stream()
                .map(TemporalValue::value)
                .toList();
        assertThat(values).containsExactly(5, 10, 15);
    }

    @Test
    public void testGetInRangeAsOfNow_case3_emptyRange() {
        BiTemporalCollection<Integer> biTemporalCollection = (BiTemporalCollection<Integer>) createInstance();
        Instant start = Instant.parse("2022-12-03T00:00:00Z");
        Instant end = Instant.parse("2022-12-03T23:59:59Z");
        TemporalRange temporalRange = TemporalRange.fromTo(start, end);

        assertThat(biTemporalCollection.getInRangeAsOfNow(temporalRange)).isEmpty();
    }
}
