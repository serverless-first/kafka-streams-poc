package com.event.framework.publisher;

import com.event.framework.core.api.Event;
import com.event.framework.core.api.IEventProducer;
import com.event.framework.core.api.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping("eventstore")
public class PublisherEndpoint {

    @Autowired
    IEventProducer<Event> producer;

    @PostMapping
    ResponseEntity publish(@RequestBody Event event) {

        try {
            producer.publish(event);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(HttpStatus.ACCEPTED);
    }
}
