package com.event.framework.core.kafka;

import com.event.framework.core.api.Event;
import com.event.framework.core.api.IEventProducer;
import com.event.framework.core.api.MessagingException;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Component
public class KafkaEventProducer<T extends Event> implements IEventProducer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaEventProducer.class);

    KafkaProducer<String, String> producer = null;
    KafkaConfiguration kafkaConfiguration;
    @Autowired
    public KafkaEventProducer(KafkaConfiguration kafkaConfiguration) {
        this.kafkaConfiguration = kafkaConfiguration;
    }

    @PreDestroy
    public void close() {
        LOGGER.info("Cleaning up the producer by closing it.");
        if (producer != null) {
            producer.flush();
            producer.close();
            producer = null;
        }

    }

    @Override
    public boolean publish(T event) throws MessagingException {
        LOGGER.info("Publishing the event {}", event.getPayload());
        try {

            KafkaProducer<String, String> sender = getKafkaProducer();
            ProducerRecord<String, String> record = new ProducerRecord<>(event.getTopic(), event.getPayload());
            Future<RecordMetadata> future = sender.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e != null) {
                        LOGGER.error(e.getMessage());
                    } else {
                        LOGGER.info("Sent successfully\n Topic: {}, Partition: {}, Offset: {}", recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
                    }
                }

            });

            RecordMetadata metadata = future.get(50, TimeUnit.MILLISECONDS);
            if (!metadata.hasOffset()) {
                return false;
            }
        } catch (Exception e) {
            throw new MessagingException(e.getCause().getMessage());
        }
        finally {
           // close();
        }
        return true;
    }

    private KafkaProducer<String, String> getKafkaProducer() {
        if (producer != null)
            return producer;

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConfiguration.getBootstrapServers());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        producer = new KafkaProducer<String, String>(properties);
        return producer;
    }
}
