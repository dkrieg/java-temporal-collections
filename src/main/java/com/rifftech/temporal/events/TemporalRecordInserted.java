package com.rifftech.temporal.events;

import com.rifftech.temporal.collections.TemporalRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class TemporalRecordInserted<T> extends TemporalEvent<T> {
    public TemporalRecordInserted(TemporalRecord<T> record) {
        super(record);
    }
}
