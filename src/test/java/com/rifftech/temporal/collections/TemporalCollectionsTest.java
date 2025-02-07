package com.rifftech.temporal.collections;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static com.rifftech.temporal.collections.TemporalRange.FOREVER;
import static com.rifftech.temporal.collections.TemporalRange.fromTo;
import static com.rifftech.temporal.collections.TemporalRange.fromToMax;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

    @Test
    public void immutableBiTemporalCollection_WhenSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenSortedListOfContiguousBiTemporalRecordsWithChangesInSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessAndSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromToMax(now.minus(Duration.ofDays(3))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        6)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenSortedListOfContiguousBiTemporalRecordsWithChangesInSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessAndSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromToMax(now.minus(Duration.ofDays(3))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        6)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenUnSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenUnSortedListOfContiguousBiTemporalRecordsWithChangesInSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenUnSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessAndSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromToMax(now.minus(Duration.ofDays(3))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenUnSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenUnSortedListOfContiguousBiTemporalRecordsWithChangesInSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(3)), now.minus(Duration.ofDays(2))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenUnSortedListOfContiguousBiTemporalRecordsWithChangesInBusinessAndSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(4))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(2)), now.minus(Duration.ofDays(1))),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(5)), now.minus(Duration.ofDays(2))),
                        fromToMax(now.minus(Duration.ofDays(3))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromToMax(now.minus(Duration.ofDays(2))),
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        5)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenSortedListOfNonContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenSortedListOfNonContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
    }

    @Test
    public void immutableBiTemporalCollection_WhenSortedListOfNonContiguousBiTemporalRecordsWithChangesInSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        2),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(2)), now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        assertThatExceptionOfType(NonContiguousSystemEffectiveRange.class).isThrownBy(() -> TemporalCollections.immutableBiTemporalCollection(temporalRecords));
    }

    @Test
    public void mutableBiTemporalCollection_WhenSortedListOfNonContiguousBiTemporalRecordsWithChangesInSystemTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        1),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        2),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        4),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromTo(now.minus(Duration.ofDays(2)), now),
                        5),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6)
        );
        assertThatExceptionOfType(NonContiguousSystemEffectiveRange.class).isThrownBy(() -> TemporalCollections.mutableBiTemporalCollection(temporalRecords));
    }

    @Test
    public void immutableBiTemporalCollection_WhenUnSortedListOfNonContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5)
        );
        BiTemporalCollection<Integer> collection = TemporalCollections.immutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }

    @Test
    public void mutableBiTemporalCollection_WhenUnSortedListOfNonContiguousBiTemporalRecordsWithChangesInBusinessTimeIsProvided() {
        Instant now = Instant.now();
        List<BiTemporalRecord<Integer>> temporalRecords = List.of(
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(8)), now.minus(Duration.ofDays(7))),
                        fromToMax(now),
                        2),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(10)), now.minus(Duration.ofDays(9))),
                        fromToMax(now),
                        1),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(4)), now.minus(Duration.ofDays(3))),
                        fromToMax(now),
                        4),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(6)), now.minus(Duration.ofDays(5))),
                        fromToMax(now),
                        3),
                new BiTemporalRecord<>(
                        fromToMax(now),
                        fromToMax(now),
                        6),
                new BiTemporalRecord<>(
                        fromTo(now.minus(Duration.ofDays(1)), now),
                        fromToMax(now),
                        5)
        );
        MutableBiTemporalCollection<Integer> collection = TemporalCollections.mutableBiTemporalCollection(temporalRecords);
        Collection<BiTemporalRecord<Integer>> inRange = collection.getInRange(FOREVER, FOREVER);
        assertThat(inRange).hasSize(temporalRecords.size());
        assertThat(inRange).element(0)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(1).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(1).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(1).value()));
        assertThat(inRange).element(1)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(0).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(0).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(0).value()));
        assertThat(inRange).element(2)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(3).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(3).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(3).value()));
        assertThat(inRange).element(3)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(2).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(2).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(2).value()));
        assertThat(inRange).element(4)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(5).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(5).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(5).value()));
        assertThat(inRange).element(5)
                .matches(record -> record.businessEffective().equals(temporalRecords.get(4).businessEffective()))
                .matches(record -> record.systemEffective().equals(temporalRecords.get(4).systemEffective()))
                .matches(record -> record.value().equals(temporalRecords.get(4).value()));
    }
}
