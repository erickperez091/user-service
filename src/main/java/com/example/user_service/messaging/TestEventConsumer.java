package com.example.user_service.messaging;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingConsumer;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Log4j2
public class TestEventConsumer implements MessagingConsumer {

    @Value("${messaging.destination.test}")
    private String destination;

    @Override
    public String destination() {
        return this.destination;
    }

    @Override
    public void consume(MessageEvent messageEvent) {
        logger.info("[TestEventConsumer][consume]Test Event: {}", messageEvent);
    }
}
