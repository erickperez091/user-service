package com.example.user_service.messaging;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPublisher {

    @Value("${messaging.destination.user}")
    private String destination;

    private final MessagingProducer messagingProducer;

    public void sendEvent(MessageEvent messageEvent) {
        messagingProducer.send(destination, messageEvent);
    }
}
