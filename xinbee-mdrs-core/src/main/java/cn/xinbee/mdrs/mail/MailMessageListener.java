package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;

public interface MailMessageListener {

    void onMessage(MailTopicMessage queue);

}
