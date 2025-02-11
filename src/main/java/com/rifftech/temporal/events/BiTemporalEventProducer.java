package com.rifftech.temporal.events;

public interface BiTemporalEventProducer<T> {
    void publish(BiTemporalEvent<T> event);
}
