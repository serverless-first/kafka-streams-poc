package com.event.framework.core.api;

import javax.validation.constraints.NotEmpty;

public class Event {
    @NotEmpty
    private String topic;

    private String payload;

    public Event(String topic, String payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
