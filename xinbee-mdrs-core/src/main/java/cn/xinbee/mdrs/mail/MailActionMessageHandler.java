package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;

public interface MailActionMessageHandler {

		Object handleMessage(MailTopicMessage message);

}
