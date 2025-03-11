package com.rifftech.temporal.events;

import com.rifftech.temporal.collections.TemporalRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Accessors(fluent = true)
@Getter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public sealed class TemporalEvent<T> permits TemporalRecordInserted, TemporalRecordUpdated, TemporalRecordDeleted {
    TemporalRecord<T> record;
    TemporalEventType eventType;
}
