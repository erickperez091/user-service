package com.example.user_service.messaging;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPublisher {
    private final MessagingProducer messagingProducer;

    public void sendEvent(MessageEvent messageEvent) {
        messagingProducer.send(messageEvent);
    }
}
