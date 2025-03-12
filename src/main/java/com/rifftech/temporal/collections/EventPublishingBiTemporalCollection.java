package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.BiTemporalEventProducer;
import com.rifftech.temporal.events.BiTemporalRecordDeleted;
import com.rifftech.temporal.events.BiTemporalRecordInserted;
import com.rifftech.temporal.events.BiTemporalRecordUpdated;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.util.function.Predicate.not;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EventPublishingBiTemporalCollection<T> implements MutableBiTemporalCollection<T> {
    ConcurrentSkipListBiTemporalCollection<T> collection;
    BiTemporalEventProducer<T> eventProducer;

    public EventPublishingBiTemporalCollection(
            @NonNull ConcurrentSkipListBiTemporalCollection<T> collection,
            @NonNull BiTemporalEventProducer<T> eventProducer) {
        this.collection = collection;
        this.eventProducer = eventProducer;
    }

    @Override
    public Optional<BiTemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime, @NonNull T item) {
        Optional<BiTemporalRecord<T>> priorValue = collection.effectiveAsOf(validTime, transactionTime, item);
        priorValue.flatMap(r -> getAsOf(r.businessEffective().start(), r.systemEffective().start()))
                .map(BiTemporalRecordUpdated::new)
                .ifPresent(eventProducer::publish);
        getAsOf(validTime, transactionTime)
                .filter(not(record -> priorValue.map(record::compareTo).map(i -> i == 0).orElse(false)))
                .map(BiTemporalRecordInserted::new)
                .ifPresent(eventProducer::publish);
        return priorValue;
    }

    @Override
    public Optional<BiTemporalRecord<T>> expireAsOf(@NonNull Instant businessTime, @NonNull Instant systemTime) {
        Optional<BiTemporalRecord<T>> asOf = getAsOf(businessTime, systemTime);
        Optional<BiTemporalRecord<T>> priorValue = expireAsOfSkipEvent(businessTime, systemTime);
        priorValue.ifPresent(record -> {
            Boolean isDelete = asOf.map(r -> businessTime.equals(r.businessEffective().start()) && systemTime.equals(r.systemEffective().start())).orElse(false);
            if (isDelete) {
                eventProducer.publish(new BiTemporalRecordDeleted<>(record));
            } else {
                eventProducer.publish(new BiTemporalRecordUpdated<>(getAsOf(record.businessEffective().start(), record.systemEffective().start()).orElseThrow()));
            }
        });
        return priorValue;
    }

    public Optional<BiTemporalRecord<T>> expireAsOfSkipEvent(@NonNull Instant businessTime, @NonNull Instant systemTime) {
        return collection.expireAsOf(businessTime, systemTime);
    }

    @Override
    public Optional<BiTemporalRecord<T>> getAsOf(@NonNull Instant validTime, @NonNull Instant transactionTime) {
        return collection.getAsOf(validTime, transactionTime);
    }

    @Override
    public Optional<BiTemporalRecord<T>> getPriorTo(@NonNull Instant validTime, @NonNull Instant transactionTime) {
        return collection.getPriorTo(validTime, transactionTime);
    }

    @Override
    public Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange) {
        return collection.getInRange(validRange);
    }

    @Override
    public Collection<BiTemporalRecord<T>> getInRange(@NonNull TemporalRange validRange, @NonNull TemporalRange transactionRange) {
        return collection.getInRange(validRange, transactionRange);
    }

    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }
}