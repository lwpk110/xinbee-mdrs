package cn.xinbee.mdrs.queue;

import org.springframework.messaging.Message;

public interface MailActionQueueTemplate {

    void send(Object message);

    Message<?> receive(Object prams);

    boolean isContains(Object prams);

}
