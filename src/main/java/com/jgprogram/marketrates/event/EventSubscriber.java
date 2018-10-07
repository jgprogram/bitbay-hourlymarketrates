package com.jgprogram.marketrates.event;

public interface EventSubscriber<T extends Event>{

    void handleEvent(T event);

    Class<T> subscribedToEventType();
}
