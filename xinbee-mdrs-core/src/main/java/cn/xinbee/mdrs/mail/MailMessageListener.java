package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicQueue;

public interface MailMessageListener {

    void onMessage(MailTopicQueue queue);

}
