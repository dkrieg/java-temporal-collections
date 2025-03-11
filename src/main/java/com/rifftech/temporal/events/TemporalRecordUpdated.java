package com.rifftech.temporal.events;

import com.rifftech.temporal.collections.TemporalRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.rifftech.temporal.events.TemporalEventType.TEMPORAL_RECORD_UPDATED;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class TemporalRecordUpdated<T> extends TemporalEvent<T> {
    public TemporalRecordUpdated(TemporalRecord<T> record) {
        super(record, TEMPORAL_RECORD_UPDATED);
    }
}
