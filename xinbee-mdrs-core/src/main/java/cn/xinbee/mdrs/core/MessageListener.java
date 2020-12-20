package cn.xinbee.mdrs.core;


import org.springframework.messaging.Message;

@FunctionalInterface
public interface MessageListener {

    void onMessage(Message<?> msg);
}
