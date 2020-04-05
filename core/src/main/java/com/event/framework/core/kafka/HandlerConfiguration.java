package com.event.framework.core.kafka;


public class HandlerConfiguration {
    private String inputTopic;
    private String handler;
    private String outputTopic;

    public HandlerConfiguration() {

    }
    public HandlerConfiguration(String inputTopic, String handler, String outputTopic) {
        this.inputTopic = inputTopic;
        this.handler = handler;
        this.outputTopic = outputTopic;
    }

    public String getInputTopic() {
        return inputTopic;
    }

    public void setInputTopic(String inputTopic) {
        this.inputTopic = inputTopic;
    }

    public String getHandler() {
        return handler;
    }

    public void setHandler(String handler) {
        this.handler = handler;
    }

    public String getOutputTopic() {
        return outputTopic;
    }

    public void setOutputTopic(String outputTopic) {
        this.outputTopic = outputTopic;
    }
}
