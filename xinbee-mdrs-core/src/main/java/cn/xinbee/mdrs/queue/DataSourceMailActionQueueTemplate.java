package cn.xinbee.mdrs.queue;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.repository.MailTopicQueueRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Map;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("unchecked")
public class DataSourceMailActionQueueTemplate implements MailActionQueueTemplate {

		public static final String TOPIC_NAME_PARAM = "topic_name";
		public static final String MESSAGE_ID = "message_id";
		public static final String MESSAGE_PRE_FETCH = "prefetch_size";


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
		@Transactional(isolation = Isolation.SERIALIZABLE)
		public Message<?> receive(Object params) {
				Map<String, Object> queryParam = (Map<String, Object>) params;
				MailTopicMessage.MessageTopic topic = (MailTopicMessage.MessageTopic) queryParam
						.get(TOPIC_NAME_PARAM);
				Object prefetchValue = queryParam.get(MESSAGE_PRE_FETCH);
				int prefetch = prefetchValue == null ? 10 : (int) prefetchValue;
				List<MailTopicMessage> topicMessages = repository
						.findAllByTopicAndLockedFalseOrderByCreatedDateDesc(topic, PageRequest.of(0, prefetch));
				if (!CollectionUtils.isEmpty(topicMessages)) {
						List<MailTopicMessage> lockData = topicMessages.stream()
								.peek(topicMessage -> topicMessage.setLocked(true)).collect(
										Collectors.toList());
						this.repository.saveAll(lockData);
						return MessageBuilder.withPayload(lockData)
								.build();
				}
				return null;
		}

		@Override
		@Transactional(isolation = Isolation.SERIALIZABLE)
		public Message<?> receiveSingle(Object prams) {
				Map<String, Object> queryParam = (Map<String, Object>) prams;
				MailTopicMessage.MessageTopic topic = (MailTopicMessage.MessageTopic) queryParam
						.get(TOPIC_NAME_PARAM);
				MailTopicMessage message = this.repository
						.findTopByTopicAndLockedFalseOrderByCreatedDateDesc(topic);
				if (null != message) {
						message.lockMessage();
						this.repository.save(message);
						return MessageBuilder.withPayload(message)
								.build();
				}
				return null;
		}

		@Override
		public boolean isContains(Object prams) {
				Map<String, Object> queryParam = (Map<String, Object>) prams;
				MailTopicMessage.MessageTopic topic = (MailTopicMessage.MessageTopic) queryParam
						.get(TOPIC_NAME_PARAM);
				return repository.existsByTopicAndLockedFalse(topic);
		}

		@Override
		public void ack(boolean success, Long msgId) {
				if (success) {
						Assert.notNull(msgId, "msgId can't be null");
						this.repository.deleteById(msgId);
				}
		}

		@Override
		public void recoveryLock(Object prams) {
				Map<String, Object> queryParam = (Map<String, Object>) prams;
				MailTopicMessage.MessageTopic topic = (MailTopicMessage.MessageTopic) queryParam
						.get(TOPIC_NAME_PARAM);
				List<MailTopicMessage> lockData = this.repository
						.findAllByTopicAndLockedTrue(topic);
				if (!CollectionUtils.isEmpty(lockData)) {
						List<MailTopicMessage> recoveryList = lockData.stream()
								.peek(MailTopicMessage::recoveryLock)
								.collect(Collectors.toList());
						this.repository.saveAll(recoveryList);
				}
		}
}
