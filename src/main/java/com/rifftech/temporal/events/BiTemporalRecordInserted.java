package com.rifftech.temporal.events;

import com.rifftech.temporal.collections.BiTemporalRecord;
import com.rifftech.temporal.collections.TemporalRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class BiTemporalRecordInserted<T> extends BiTemporalEvent<T> {
    public BiTemporalRecordInserted(BiTemporalRecord<T> record) {
        super(record);
    }
}
