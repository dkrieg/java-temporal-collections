package com.rifftech.temporal.events;

import com.rifftech.temporal.collections.BiTemporalRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class BiTemporalRecordDeleted<T> extends BiTemporalEvent<T> {
    public BiTemporalRecordDeleted(BiTemporalRecord<T> record) {
        super(record);
    }
}
