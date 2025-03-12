package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.TemporalEventProducer;
import com.rifftech.temporal.events.TemporalRecordDeleted;
import com.rifftech.temporal.events.TemporalRecordInserted;
import com.rifftech.temporal.events.TemporalRecordUpdated;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TemporalCollectionsTest {

    @Test
    public void immutableTemporalCollection_WhenSortedListOfContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalCollection<Integer> collection = TemporalCollections.immutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void immutableTemporalCollection_WhenSortedListOfContiguousTemporalRecordsIsProvided_WithGapDueToExpiredEntry() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalCollection<Integer> collection = TemporalCollections.immutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(5);
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(collection.getAsOf(now.minus(Duration.ofDays(3)))).isEmpty();
    }

    @Test
    public void mutableTemporalCollection_WhenSortedListOfContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void mutableTemporalCollection_WithEventProducer_effectiveAsOf_ValidTimeRangeMatchesExactly() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords, mockEventProducer);
        Integer expectedValue = 0;
        TemporalRecord<Integer> expectedRecord = new TemporalRecord<>(
                temporalRecords.get(0).validRange(),
                expectedValue);

        Optional<TemporalRecord<Integer>> result = collection.effectiveAsOf(expectedRecord.validRange().start(), expectedValue);

        ArgumentCaptor<TemporalRecordInserted<Integer>> insertCaptor = ArgumentCaptor.forClass(TemporalRecordInserted.class);
        ArgumentCaptor<TemporalRecordUpdated<Integer>> updateCaptor = ArgumentCaptor.forClass(TemporalRecordUpdated.class);
        verify(mockEventProducer, never()).publish(insertCaptor.capture());
        verify(mockEventProducer, times(1)).publish(updateCaptor.capture());

        assertThat(updateCaptor.getValue().record()).isEqualTo(expectedRecord);
        assertThat(result).hasValue(temporalRecords.get(0));
    }

    @Test
    public void mutableTemporalCollection_WithEventProducer_effectiveAsOf_NoPriorRecordFound() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords, mockEventProducer);
        Integer expectedValue = 0;
        TemporalRecord<Integer> expectedRecord = new TemporalRecord<>(
                fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                expectedValue);

        Optional<TemporalRecord<Integer>> result = collection.effectiveAsOf(expectedRecord.validRange().start(), expectedValue);

        ArgumentCaptor<TemporalRecordInserted<Integer>> insertCaptor = ArgumentCaptor.forClass(TemporalRecordInserted.class);
        ArgumentCaptor<TemporalRecordUpdated<Integer>> updateCaptor = ArgumentCaptor.forClass(TemporalRecordUpdated.class);
        verify(mockEventProducer, times(1)).publish(insertCaptor.capture());
        verify(mockEventProducer, never()).publish(updateCaptor.capture());

        assertThat(insertCaptor.getValue().record()).isEqualTo(expectedRecord);
        assertThat(result).isEmpty();
    }

    @Test
    public void mutableTemporalCollection_WithEventProducer_effectiveAsOf_PriorRecordFound() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords, mockEventProducer);
        Integer expectedValue = 7;
        TemporalRecord<Integer> expectedInsert = new TemporalRecord<>(
                fromToMax(now.plus(Duration.ofDays(1))),
                expectedValue);
        TemporalRecord<Integer> expectedUpdate = temporalRecords.get(5);

        Optional<TemporalRecord<Integer>> result = collection.effectiveAsOf(expectedInsert.validRange().start(), expectedValue);

        ArgumentCaptor<TemporalRecordInserted<Integer>> insertCaptor = ArgumentCaptor.forClass(TemporalRecordInserted.class);
        ArgumentCaptor<TemporalRecordUpdated<Integer>> updateCaptor = ArgumentCaptor.forClass(TemporalRecordUpdated.class);
        verify(mockEventProducer, times(1)).publish(insertCaptor.capture());
        verify(mockEventProducer, times(1)).publish(updateCaptor.capture());

        assertThat(insertCaptor.getValue().record()).isEqualTo(expectedInsert);
        assertThat(result).hasValue(expectedUpdate);
    }

    @Test
    public void mutableTemporalCollection_WithEventProducer_expireAsOf_WithExpireAtExactStartOfRecord() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords, mockEventProducer);
        TemporalRecord<Integer> expectedRecord = temporalRecords.get(0);

        Optional<TemporalRecord<Integer>> result = collection.expireAsOf(expectedRecord.validRange().start());

        ArgumentCaptor<TemporalRecordDeleted<Integer>> deleteCaptor = ArgumentCaptor.forClass(TemporalRecordDeleted.class);
        ArgumentCaptor<TemporalRecordUpdated<Integer>> updateCaptor = ArgumentCaptor.forClass(TemporalRecordUpdated.class);
        verify(mockEventProducer, times(1)).publish(deleteCaptor.capture());
        verify(mockEventProducer, never()).publish(updateCaptor.capture());

        assertThat(deleteCaptor.getValue().record()).isEqualTo(expectedRecord);
        assertThat(result).hasValue(expectedRecord);
    }

    @Test
    public void mutableTemporalCollection_WithEventProducer_expireAsOf_WithStartWithRecordValidRange() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        EventPublishingTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords, mockEventProducer);
        Instant expireAt = now.minus(Duration.ofDays(5)).plus(Duration.ofHours(8));
        TemporalRecord<Integer> expectedRecord = new TemporalRecord<>(
                fromTo(now.minus(Duration.ofDays(5)), expireAt),
                1);

        Optional<TemporalRecord<Integer>> result = collection.expireAsOf(expireAt);

        ArgumentCaptor<TemporalRecordDeleted<Integer>> deleteCaptor = ArgumentCaptor.forClass(TemporalRecordDeleted.class);
        ArgumentCaptor<TemporalRecordUpdated<Integer>> updateCaptor = ArgumentCaptor.forClass(TemporalRecordUpdated.class);
        verify(mockEventProducer, never()).publish(deleteCaptor.capture());
        verify(mockEventProducer, times(1)).publish(updateCaptor.capture());

        assertThat(updateCaptor.getValue().record()).isEqualTo(expectedRecord);
        assertThat(result).hasValue(temporalRecords.get(0));
    }

    @Test
    public void mutableTemporalCollection_WithEventProducer_expireAsOf_NoPriorRecordFound() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalEventProducer<Integer> mockEventProducer = Mockito.mock(TemporalEventProducer.class);
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords, mockEventProducer);

        Optional<TemporalRecord<Integer>> result = collection.expireAsOf(now.minus(Duration.ofDays(6)));

        ArgumentCaptor<TemporalRecordDeleted<Integer>> deleteCaptor = ArgumentCaptor.forClass(TemporalRecordDeleted.class);
        ArgumentCaptor<TemporalRecordUpdated<Integer>> updateCaptor = ArgumentCaptor.forClass(TemporalRecordUpdated.class);
        verify(mockEventProducer, never()).publish(deleteCaptor.capture());
        verify(mockEventProducer, never()).publish(updateCaptor.capture());

        assertThat(result).isEmpty();
    }

    @Test
    public void mutableTemporalCollection_WhenSortedListOfContiguousTemporalRecordsIsProvided_WithGapDueToExpiredEntry() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(5);
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(collection.getAsOf(now.minus(Duration.ofDays(3)))).isEmpty();
    }

    @Test
    public void immutableTemporalCollection_WhenSortedListOfNonContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        TemporalCollection<Integer> collection = TemporalCollections.immutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void mutableTemporalCollection_WhenSortedListOfNonContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        3),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new TemporalRecord<>(
                        fromToMax(now),
                        6)
        );
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void immutableTemporalCollection_WhenUnSortedListOfContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromToMax(now),
                        6),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        TemporalCollection<Integer> collection = TemporalCollections.immutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void mutableTemporalCollection_WhenUnSortedListOfContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new TemporalRecord<>(
                        fromToMax(now),
                        6),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void immutableTemporalCollection_WhenUnSortedListOfNonContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        3),
                new TemporalRecord<>(
                        fromToMax(now),
                        6),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        TemporalCollection<Integer> collection = TemporalCollections.immutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void mutableTemporalCollection_WhenUnSortedListOfNonContiguousTemporalRecordsIsProvided() {
        Instant now = Instant.now();
        List<TemporalRecord<Integer>> temporalRecords = List.of(
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        2),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        1),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        4),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        3),
                new TemporalRecord<>(
                        fromToMax(now),
                        6),
                new TemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        MutableTemporalCollection<Integer> collection = TemporalCollections.mutableTemporalCollection(temporalRecords);
        Collection<TemporalRecord<Integer>> inRange = collection.getInRange(FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.validRange().equals(temporalRecords.get(1).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.validRange().equals(temporalRecords.get(0).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.validRange().equals(temporalRecords.get(3).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.validRange().equals(temporalRecords.get(2).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.validRange().equals(temporalRecords.get(5).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.validRange().equals(temporalRecords.get(4).validRange()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }
}
