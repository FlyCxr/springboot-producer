package com.springboot.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class TestConsumerListener {

    private static final Logger logger = LoggerFactory.getLogger(TestConsumerListener.class);

    @KafkaListener(topics = "demolog2")
    public void listen (ConsumerRecord<?, ?> record) throws Exception {
        logger.info(record.toString());
    }

}
