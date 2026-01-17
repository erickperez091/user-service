package com.example.user_service.messaging;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducerV1;
import lombok.RequiredArgsConstructor;

//@Service
@RequiredArgsConstructor
public class UserPublisherV1 {
    private final MessagingProducerV1 messagingProducerV1;

    public void sendEvent(MessageEvent messageEvent) {
        messagingProducerV1.send(messageEvent);
    }
}
