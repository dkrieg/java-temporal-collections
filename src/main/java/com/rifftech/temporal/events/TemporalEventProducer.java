package com.rifftech.temporal.events;

public interface TemporalEventProducer<T> {
    void publish(TemporalEvent<T> event);
}
