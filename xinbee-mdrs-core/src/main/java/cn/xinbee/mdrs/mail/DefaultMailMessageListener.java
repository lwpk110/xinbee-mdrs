package cn.xinbee.mdrs.mail;

import static cn.xinbee.mdrs.queue.DataSourceMailActionQueueTemplate.TOPIC_NAME_PARAM;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage.MessageTopic;
import cn.xinbee.mdrs.data.jpa.repository.MailActionRepository;
import cn.xinbee.mdrs.mail.DefaultMailActionMessageHandler.HandlerResult;
import cn.xinbee.mdrs.queue.MailActionQueueTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.Message;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class DefaultMailMessageListener implements MailMessageListener, SmartLifecycle {

		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		private boolean abortListen = false;
		private volatile boolean hasMsg = false;
		private final MailActionQueueTemplate queueTemplate;
		private final MailActionRepository actionRepository;
		private final Object consumeMonitor = new Object();


		public DefaultMailMessageListener(MailActionQueueTemplate queueTemplate,
				MailActionRepository actionRepository) {
				this.queueTemplate = queueTemplate;
				this.actionRepository = actionRepository;
		}

		@Override
		public void onMessage(MailTopicMessage queue) {
				while (hasMsg) {
						listenMessage();
				}
		}

		@SuppressWarnings("unchecked")
		private void listenMessage() {
				final Message<?> msg = queueTemplate.receive(
						Collections.singletonMap(TOPIC_NAME_PARAM, MessageTopic.MAIL_ACTION));
				if (msg == null) {
						synchronized (consumeMonitor) {
								hasMsg = false;
						}
						return;
				}
				List<MailTopicMessage> topicMessages = (List<MailTopicMessage>) msg.getPayload();
				ExecutorService executorService = Executors.newFixedThreadPool(topicMessages.size());
				List<Future<HandlerResult>> futures = asyncHandle(topicMessages, executorService);
				doAck(futures);
				executorService.shutdownNow();
		}

		private List<Future<HandlerResult>> asyncHandle(List<MailTopicMessage> topicMessages,
				ExecutorService executorService) {
				int size = topicMessages.size();
				List<Future<HandlerResult>> futures = new ArrayList<>(size);
				for (MailTopicMessage topicMessage : topicMessages) {
						Future<HandlerResult> future = executorService
								.submit(new DefaultMailActionMessageHandler(topicMessage, actionRepository));
						futures.add(future);
				}
				return futures;
		}

		private void doAck(List<Future<HandlerResult>> futures) {
				CopyOnWriteArrayList<Future<HandlerResult>> list = new CopyOnWriteArrayList<>(
						futures); //thread-safe list remove
				do {
						for (Future<HandlerResult> f : list) {
								try {
										HandlerResult result = f.get();
										if (f.isDone() && result.isSuccess()) {
												Long msgId = result.getMsgId();
												this.queueTemplate.ack(true, msgId);
												list.remove(f);
										}
								} catch (Exception e) {
										list.remove(f);
										logger.error("do ack 失败", e);
								}
						}
				} while (list.size() > 0);
		}

		@Override
		@Transactional(isolation = Isolation.SERIALIZABLE)
		public void start() {
				ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
				executorService.scheduleWithFixedDelay(() -> {
						try {
								Map<String, Object> queryParams = new HashMap<>();
								queryParams.put(TOPIC_NAME_PARAM, MessageTopic.MAIL_ACTION);
								synchronized (consumeMonitor) {
										hasMsg = queueTemplate.isContains(queryParams);
								}
								if (hasMsg) {
										onMessage(new MailTopicMessage(MessageTopic.MAIL_ACTION));
								}
						} catch (Exception e) {
								logger.error("schedule poll message fail...", e);
						}
				}, 3000, 10000, TimeUnit.MILLISECONDS);
		}

		@Override
		public void stop() {
				abortListen = true;
		}

		@Override
		public boolean isRunning() {
				return false;
		}
}

