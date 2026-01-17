package com.example.user_service.messaging;

import com.example.common.entity.EnumUtil;
import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TestPublisherV2 {

    private final MessagingProducer messagingProducer;

    public void sendEvent(){
        Map<String, Object> payload = new HashMap<>();
        payload.put("message", "Test Event");
        MessageEvent messageEvent = new MessageEvent(EnumUtil.EventType.TEST_EVENT, payload);
        messagingProducer.send("test", messageEvent);
    }
}
