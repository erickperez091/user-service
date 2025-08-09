package com.example.user_service.messaging;

import com.example.common.entity.MessageEvent;
import com.example.common.service.messaging.MessagingCosumer;
import com.example.common.utilities.ConverterUtil;
import com.example.user_service.entity.User;
import com.example.user_service.services.UserProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserEventConsumer implements MessagingCosumer {

    private final UserProcessor userProcessor;
    private final ConverterUtil converterUtil;

    @Override
    public void consume(MessageEvent messageEvent) {
        switch (messageEvent.getEventName()) {
            case CREATE_USER -> {
                User user = this.converterUtil.mapToObject(messageEvent.getPayload(), User.class);
                this.userProcessor.saveUser(user);
                logger.info("[UserEventConsumer][CREATE_USER]: {}", user);
            }
            case UPDATE_USER -> throw new NotImplementedException("Update User not implemented");
            case DELETE_USER -> throw new NotImplementedException("Delete User not implemented");
            default -> System.out.println("Event not supported");
        }
    }
}
