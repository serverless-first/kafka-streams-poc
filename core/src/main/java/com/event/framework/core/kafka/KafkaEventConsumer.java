package com.event.framework.core.kafka;

import com.event.framework.core.api.Event;
import com.event.framework.core.api.IEventHandler;
import com.event.framework.core.api.MessagingException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static java.util.stream.Collectors.toList;

@Component
public class KafkaEventConsumer<T extends Event>  {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventConsumer.class);
    KafkaConfiguration kafkaConfiguration;
    private HashMap<String, IEventHandler<T>> handlers = new HashMap<>();

    @Autowired
    private ApplicationContext context;

    @Autowired
    public KafkaEventConsumer(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
    }

    @PostConstruct
    public void setup() {
//        List<String> topics = this.kafkaConfiguration.handlers.stream().map(handlerConfiguration -> handlerConfiguration.getInputTopic()).collect(toList());

        Properties properties = buildKafkaProperties();
        Topology topology = buildTopology();

        KafkaStreams streams = new KafkaStreams(topology, properties);
        streams.start();
//        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConfiguration.groupId);


//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
//        consumer.subscribe(topics);

    }

    public Topology buildTopology() {
        this.kafkaConfiguration.handlers.stream().forEach(handlerConfiguration -> setupHandler(handlerConfiguration));
        StreamsBuilder builder = new StreamsBuilder();

        this.kafkaConfiguration.handlers.stream().forEach(handlerConfiguration -> {
            KStream<String, String> stream = builder.stream(handlerConfiguration.getInputTopic());
            stream.map((key, input) -> {
                String output = handlers.get(handlerConfiguration.getInputTopic()).handle(input);
                return new KeyValue<>(key, output);
            });

            if (!handlerConfiguration.getOutputTopic().equals("none"))
                stream.to(handlerConfiguration.getOutputTopic());
        });

        return builder.build();
    }

    public Properties buildKafkaProperties() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, kafkaConfiguration.groupId);
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        return properties;
    }

    void setupHandler(HandlerConfiguration handlerConfiguration) throws MessagingException {

        try {
            Class handlerClass = Class.forName(handlerConfiguration.getHandler());
            try {
                IEventHandler handler = (IEventHandler) context.getBean(handlerClass);
                handlers.put(handlerConfiguration.getInputTopic(), handler);
            } catch (BeansException e) {
                e.printStackTrace();
                throw new MessagingException(e);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new MessagingException(e);
        }
    }

 }
