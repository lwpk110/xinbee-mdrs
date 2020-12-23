package cn.xinbee.mdrs.mail;

import static cn.xinbee.mdrs.queue.DataSourceMailActionQueueTemplate.TOPIC_NAME_PARAM;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage.MessageTopic;
import cn.xinbee.mdrs.data.jpa.repository.MailActionRepository;
import cn.xinbee.mdrs.mail.DefaultMailActionMessageHandler.HandlerResult;
import cn.xinbee.mdrs.queue.MailActionQueueTemplate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
		ExecutorService executorService = Executors.newFixedThreadPool(4);

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

		//流程编排
		private void listenMessage() {
				CompletableFuture<? extends Message<?>> cf = CompletableFuture
						.supplyAsync(this::fetchMessage, executorService);
				Message<?> message = null;
				try {
						message = cf.get();
				} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
				} catch (ExecutionException e) {
						e.printStackTrace();
				}
				if (message == null) {
						cf.cancel(true);
				} else {
						cf.thenApplyAsync(this::doHandle,executorService) //
								.exceptionally(this::handleException)
								.thenAccept(this::singleAck);
				}
				if(executorService.isTerminated()){
						executorService.shutdownNow();
				}
		}

		private Message<?> fetchMessage() {
				Map<String, Object> params = createMailActionTopicParams();
				Message<?> message = queueTemplate.receiveSingle(params);
				if (message == null) {
						synchronized (consumeMonitor) {
								this.hasMsg = false;
						}
						return null;
				}
				return message;
		}

		private Map<String, Object> createMailActionTopicParams() {
				return Collections
						.singletonMap(TOPIC_NAME_PARAM, MessageTopic.MAIL_ACTION);
		}

		private HandlerResult doHandle(Message<?> message) {
				MailTopicMessage topicMessage = (MailTopicMessage) message.getPayload();
				DefaultMailActionMessageHandler handler = new DefaultMailActionMessageHandler(
						topicMessage, actionRepository);
				try {
						return handler.call();
				} catch (Exception e) {
						throw new IllegalThreadStateException(
								"DefaultMailActionMessageHandler 处理异常...");
				}
		}

		private HandlerResult handleException(Throwable throwable) {
				logger.error("mail action thread 处理异常...", throwable);
				return new HandlerResult(false, null);
		}

		private void singleAck(HandlerResult result) {
				if (result != null) {
						Long msgId = result.getMsgId();
						this.queueTemplate.ack(true, msgId);
				}
		}

		@Override
		@Transactional(isolation = Isolation.SERIALIZABLE)
		public void start() {
				recoveryLock();
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

		/**
		 *服务重启后，恢复未ack消息
		 */
		private void recoveryLock() {
				this.queueTemplate.recoveryLock(createMailActionTopicParams());
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

