package com.rifftech.temporal.events;

import com.rifftech.temporal.collections.TemporalRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class TemporalRecordDeleted<T> extends TemporalEvent<T> {
    public TemporalRecordDeleted(TemporalRecord<T> record) {
        super(record);
    }
}
