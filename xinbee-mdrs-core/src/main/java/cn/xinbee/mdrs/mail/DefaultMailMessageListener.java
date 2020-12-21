package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailAction;
import cn.xinbee.mdrs.data.jpa.domain.MailAction.ActionStatus;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicQueue;
import cn.xinbee.mdrs.data.jpa.repository.MailActionRepository;
import cn.xinbee.mdrs.queue.MailActionQueueTemplate;
import cn.xinbee.mdrs.service.MailMessageServiceImpl.MailOpenActionDto;
import cn.xinbee.mdrs.service.dto.MailOpenTrackInf;
import cn.xinbee.mdrs.util.JsonUtils;
import java.nio.charset.StandardCharsets;
import org.joda.time.DateTime;
import org.springframework.context.SmartLifecycle;
import org.springframework.messaging.Message;
import org.springframework.util.Base64Utils;

public class DefaultMailMessageListener implements MailMessageListener, SmartLifecycle {

    private boolean abortListen = false;
    private final MailActionQueueTemplate queueTemplate;
    private final MailActionRepository actionRepository;

    public DefaultMailMessageListener(MailActionQueueTemplate queueTemplate,
        MailActionRepository actionRepository) {
        this.queueTemplate = queueTemplate;
        this.actionRepository = actionRepository;
    }

    @Override
    public void onMessage(MailTopicQueue queue) {
        do {
            new Thread(() -> {
                final Message<?> msg = queueTemplate.receive();
                final MailOpenActionDto openActionDto;
                try {
                    openActionDto = convert(msg);
                } catch (Exception e) {
                    throw new IllegalStateException(String.format("转换消息失败，msg:%s", msg), e);
                }
                handle(openActionDto);
            }, "consume_mail_action").start();
        } while (!abortListen);

    }

    private MailOpenActionDto convert(Message<?> msg) {
        final Object payload = msg.getPayload();
        MailTopicQueue topicQueue = (MailTopicQueue) payload;
        final String mailActionStr = topicQueue.getMessage();
        return JsonUtils.deserialize(mailActionStr, MailOpenActionDto.class);
    }

    private void handle(MailOpenActionDto dto ){
        final DateTime actionDate = dto.getActionDate();
        final String base64TrackInf = dto.getBase64TrackInf();
        final byte[] openTrackByte = Base64Utils.decodeFromString(base64TrackInf);
        final String opeTrackStr = new String(openTrackByte, StandardCharsets.UTF_8);
        final MailOpenTrackInf openTrackInf = JsonUtils
            .deserialize(opeTrackStr, MailOpenTrackInf.class);
        final MailAction mailAction = transform(openTrackInf, actionDate);
        this.actionRepository.save(mailAction);

    }

    private MailAction transform(MailOpenTrackInf openTrackInf,  DateTime actionDate){
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
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }, "")
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
