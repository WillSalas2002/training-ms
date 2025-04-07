package com.epam.training.listener;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DLQListener {

    @JmsListener(destination = "DLQ", containerFactory = "jmsListenerContainerFactory")
    public void receiveMessage(Message message) throws JMSException {
        log.info("DLQ received message: {}", message.getBody(String.class));
    }
}
