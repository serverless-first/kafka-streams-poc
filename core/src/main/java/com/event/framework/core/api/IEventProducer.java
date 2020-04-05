package com.event.framework.core.api;

public interface IEventProducer<T extends Event> {
    default boolean publish(T event) throws MessagingException {
        return false;
    }
}
