package com.rifftech.temporal.collections;

import java.util.Iterator;

record SynchronizedIterator<U>(Iterator<U> iterator) implements Iterator<U> {

    @Override
    public synchronized boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public synchronized U next() {
        return iterator.next();
    }

    @Override
    public synchronized void remove() {
        iterator.remove();
    }
}
