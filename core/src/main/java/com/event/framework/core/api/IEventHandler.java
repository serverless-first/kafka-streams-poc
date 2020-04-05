package com.event.framework.core.api;

public interface IEventHandler<T extends Event> {
    String handle(String event) throws MessagingException;
}
