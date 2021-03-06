package com.jgprogram.common.event;

import java.util.HashSet;
import java.util.Set;

public class EventBus {
    private static EventBus instance;
    private final Set<EventSubscriber<Event>> subscribers = new HashSet<>();

    private EventBus() {
    }

    public static EventBus instance() {
        if (instance == null) {
            instance = new EventBus();
        }

        return instance;
    }

    public synchronized  <T extends EventSubscriber> void subscribe(T subscriber) {
        subscribers.add(subscriber);
    }

    public synchronized <T extends Event> void publish(T aEvent) {
        Class<?> aDomainEventClass = aEvent.getClass();

        subscribers.stream()
                .filter(s -> s.subscribedToEventType().equals(aDomainEventClass))
                .forEach(s -> s.handleEvent(aEvent));
    }
}
