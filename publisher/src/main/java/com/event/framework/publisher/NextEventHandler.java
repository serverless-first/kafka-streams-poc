package com.event.framework.publisher;

import com.event.framework.core.api.Event;
import com.event.framework.core.api.IEventHandler;
import com.event.framework.core.api.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NextEventHandler implements IEventHandler<Event> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NextEventHandler.class);

    @Autowired
    SomeotherService someotherService;

    @Override
    public String handle(String event) throws MessagingException {
        LOGGER.info("Next event handler Received event {}", event);
        someotherService.process(event);
        return "output";
    }

}
