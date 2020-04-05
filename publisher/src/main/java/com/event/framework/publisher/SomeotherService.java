package com.event.framework.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SomeotherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SomeotherService.class);

    public void process(String input) {
        LOGGER.info("processing the input {}", input);

    }
}
