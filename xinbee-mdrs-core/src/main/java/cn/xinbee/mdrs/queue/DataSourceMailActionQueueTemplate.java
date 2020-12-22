package cn.xinbee.mdrs.queue;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.repository.MailTopicQueueRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;

@SuppressWarnings("unchecked")
public class DataSourceMailActionQueueTemplate implements MailActionQueueTemplate {
    public static final String TOPIC_NAME_PARAM = "topic_name";

    private final MailTopicQueueRepository repository;

    public DataSourceMailActionQueueTemplate(MailTopicQueueRepository repository) {
        this.repository = repository;
    }

    @Override
    public void send(Object message) {
        MailTopicMessage topicQueue = (MailTopicMessage) message;
        repository.save(topicQueue);
    }

    @Override
    public Message<?> receive(Object params) {
        Map<String, Object> queryParam = (Map<String, Object>) params;
        MailTopicMessage.MessageTopic topic = (MailTopicMessage.MessageTopic) queryParam.get(TOPIC_NAME_PARAM);
        MailTopicMessage topicMessage = repository.findTopByTopicOrderByCreatedDateDesc(topic);
        return MessageBuilder.withPayload(topicMessage).build();
    }

    @Override
    public boolean isContains(Object prams) {
        Map<String, Object> queryParam = (Map<String, Object>) prams;
        MailTopicMessage.MessageTopic topic = (MailTopicMessage.MessageTopic) queryParam.get(TOPIC_NAME_PARAM);
        return repository.existsByTopic(topic);
    }
}
