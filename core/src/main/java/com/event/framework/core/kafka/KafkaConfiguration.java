package com.event.framework.core.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "chakra")
public class KafkaConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfiguration.class);

    String groupId;
    String bootstrapServers;
    List<HandlerConfiguration> handlers = new ArrayList<HandlerConfiguration>();

    public KafkaConfiguration() {
        LOGGER.info("Constructing..");
    }

    public String getBootstrapServers() {
        if (bootstrapServers == null || bootstrapServers.isEmpty()) {
            return "localhost:9092";
        }
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<HandlerConfiguration> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<HandlerConfiguration> handlers) {
        this.handlers = handlers;
    }
}
