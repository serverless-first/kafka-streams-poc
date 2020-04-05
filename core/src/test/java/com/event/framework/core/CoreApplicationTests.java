package com.event.framework.core;

import com.event.framework.core.kafka.KafkaEventConsumer;
import com.event.framework.core.kafka.KafkaEventProducer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.event.KafkaEvent;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.Properties;

@SpringBootTest(classes = {TestApplication.class})
//@EmbeddedKafka(topics = {"start", "OffsetTransaction", "FinalTransaction"}, controlledShutdown = true)
class CoreApplicationTests {
    private TopologyTestDriver testDriver;
    private StringDeserializer stringDeserializer = new StringDeserializer();
    private ConsumerRecordFactory<String, String> recordFactory = new ConsumerRecordFactory<String, String>(new StringSerializer(), new StringSerializer());

    @Test
    void contextLoads() {
    }

    @BeforeEach
    public void setup() {

    }
}
