package cn.xinbee.mdrs.queue;

import org.springframework.messaging.Message;

public class DataSourceMailActionQueueTemplate implements MailActionQueueTemplate {

    @Override
    public void send(Object message) {

    }

    @Override
    public Message<?> receive() {
        return null;
    }

    @Override
    public boolean isContains(Object prams) {
        return false;
    }
}
