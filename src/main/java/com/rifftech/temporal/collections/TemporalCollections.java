package com.rifftech.temporal.collections;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TemporalCollections {

    public static <T extends TemporalObject> TemporalCollection<T> immutableTemporalCollection(TemporalCollection<T> toWrap) {
        return new ImmutableTemporalCollection<>(toWrap);
    }

    public static <T extends BiTemporal> BiTemporalCollection<T> immutableBiTemporalCollection(BiTemporalCollection<T> toWrap) {
        return new ImmutableBiTemporalCollection<>(toWrap);
    }

    public static <K, V extends TemporalObject> TemporalMap<K, V> immutableTemporalMap(TemporalMap<K, V> toWrap) {
        return new ImmutableTemporalMap<>(toWrap);
    }

    public static <K, V extends BiTemporal> BiTemporalMap<K, V> immutableBiTemporalMap(BiTemporalMap<K, V> toWrap) {
        return new ImmutableBiTemporalMap<>(toWrap);
    }
}
