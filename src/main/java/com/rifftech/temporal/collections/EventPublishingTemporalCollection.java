package com.rifftech.temporal.collections;

import com.rifftech.temporal.events.TemporalEventProducer;
import com.rifftech.temporal.events.TemporalRecordDeleted;
import com.rifftech.temporal.events.TemporalRecordInserted;
import com.rifftech.temporal.events.TemporalRecordUpdated;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;

import static java.util.function.Predicate.not;
import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(makeFinal = true, level = PRIVATE)
public class EventPublishingTemporalCollection<T> implements MutableTemporalCollection<T> {
    ConcurrentSkipListTemporalCollection<T> collection;
    TemporalEventProducer<T> eventProducer;

    public EventPublishingTemporalCollection(
            @NonNull ConcurrentSkipListTemporalCollection<T> collection,
            @NonNull TemporalEventProducer<T> eventProducer) {
        this.collection = collection;
        this.eventProducer = eventProducer;
    }

    @Override
    public Optional<TemporalRecord<T>> effectiveAsOf(@NonNull Instant validTime, @NonNull T item) {
        Optional<TemporalRecord<T>> priorValue = effectiveAsOfSkipEvent(validTime, item);
        priorValue.flatMap(r -> getAsOf(r.validRange().start()))
                .map(TemporalRecordUpdated::new)
                .ifPresent(eventProducer::publish);
        getAsOf(validTime)
                .filter(not(record -> priorValue.map(record::compareTo).map(i -> i == 0).orElse(false)))
                .map(TemporalRecordInserted::new)
                .ifPresent(eventProducer::publish);
        return priorValue;
    }

    public Optional<TemporalRecord<T>> effectiveAsOfSkipEvent(@NonNull Instant validTime, @NonNull T item) {
        return collection.effectiveAsOf(validTime, item);
    }

    @Override
    public Optional<TemporalRecord<T>> expireAsOf(@NonNull Instant expireAt) {
        Optional<TemporalRecord<T>> asOf = getAsOf(expireAt);
        Optional<TemporalRecord<T>> priorValue = expireAsOfSkipEvent(expireAt);
        priorValue.ifPresent(record -> {
            Boolean isDeleted = asOf.map(r -> expireAt.equals(r.validRange().start())).orElse(false);
            if (isDeleted) {
                eventProducer.publish(new TemporalRecordDeleted<>(record));
            } else {
                eventProducer.publish(new TemporalRecordUpdated<>(getAsOf(record.validRange().start()).orElseThrow()));
            }
        });
        return priorValue;
    }

    public Optional<TemporalRecord<T>> expireAsOfSkipEvent(@NonNull Instant expireAt) {
        return collection.expireAsOf(expireAt);
    }

    @Override
    public Optional<TemporalRecord<T>> getAsOf(@NonNull Instant validTime) {
        return collection.getAsOf(validTime);
    }

    @Override
    public Optional<TemporalRecord<T>> getPriorTo(@NonNull Instant validTime) {
        return collection.getPriorTo(validTime);
    }

    @Override
    public Collection<TemporalRecord<T>> getInRange(@NonNull TemporalRange validRange) {
        return collection.getInRange(validRange);
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