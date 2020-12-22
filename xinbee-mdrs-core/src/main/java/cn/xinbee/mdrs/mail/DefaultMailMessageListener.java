package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailAction;
import cn.xinbee.mdrs.data.jpa.domain.MailAction.ActionStatus;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.repository.MailActionRepository;
import cn.xinbee.mdrs.queue.MailActionQueueTemplate;
import cn.xinbee.mdrs.service.MailMessageServiceImpl.MailOpenActionDto;
import cn.xinbee.mdrs.service.dto.MailOpenTrackInf;
import cn.xinbee.mdrs.util.JsonUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import org.joda.time.DateTime;
import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.Message;
import org.springframework.util.Base64Utils;

import static cn.xinbee.mdrs.queue.DataSourceMailActionQueueTemplate.TOPIC_NAME_PARAM;

public class DefaultMailMessageListener implements MailMessageListener, SmartLifecycle {

    private boolean abortListen = false;
    private volatile boolean hasMsg = false;
    private final MailActionQueueTemplate queueTemplate;
    private final MailActionRepository actionRepository;

    public DefaultMailMessageListener(MailActionQueueTemplate queueTemplate,
                                      MailActionRepository actionRepository) {
        this.queueTemplate = queueTemplate;
        this.actionRepository = actionRepository;
    }

    @Override
    public void onMessage(MailTopicMessage queue) {
        new Thread(() -> {
            while (!abortListen && hasMsg) {
                final Message<?> msg = queueTemplate.receive(
                        Collections.singletonMap(TOPIC_NAME_PARAM, MailTopicMessage.MessageTopic.MAIL_ACTION));
                if (msg == null) {
                    hasMsg = false;
                    return;
                }
                final MailOpenActionDto openActionDto;
                try {
                    openActionDto = convert(msg);
                } catch (Exception e) {
                    throw new IllegalStateException(String.format("转换消息失败，msg:%s", msg), e);
                }
                handle(openActionDto);
            }
        }, "consume_mail_action").start();
    }

    private MailOpenActionDto convert(Message<?> msg) {
        final Object payload = msg.getPayload();
        MailTopicMessage topicQueue = (MailTopicMessage) payload;
        final String mailActionStr = topicQueue.getMessage();
        return JsonUtils.deserialize(mailActionStr, MailOpenActionDto.class);
    }

    private void handle(MailOpenActionDto dto) {
        final DateTime actionDate = dto.getActionDate();
        final String base64TrackInf = dto.getBase64TrackInf();
        final byte[] openTrackByte = Base64Utils.decodeFromString(base64TrackInf);
        final String opeTrackStr = new String(openTrackByte, StandardCharsets.UTF_8);
        final MailOpenTrackInf openTrackInf = JsonUtils
                .deserialize(opeTrackStr, MailOpenTrackInf.class);
        final MailAction mailAction = transform(openTrackInf, actionDate);
        this.actionRepository.save(mailAction);
    }

    private MailAction transform(MailOpenTrackInf openTrackInf, DateTime actionDate) {
        MailAction action = new MailAction();
        action.setActionDate(actionDate);
        action.setApiUser(openTrackInf.getApiUser());
        action.setEmail(openTrackInf.getEmail());
        action.setMailingId(openTrackInf.getEmailId());
        action.setStatus(ActionStatus.OPEN);
        action.setTaskId(openTrackInf.getTaskId());
        return action;
    }

    @Override
    public void start() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(() -> {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put(TOPIC_NAME_PARAM, MailTopicMessage.MessageTopic.MAIL_ACTION);
            hasMsg = queueTemplate.isContains(queryParams);
            onMessage(new MailTopicMessage(MailTopicMessage.MessageTopic.MAIL_ACTION));
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
