package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage.MessageTopic;
import cn.xinbee.mdrs.service.MailMessageServiceImpl.MailOpenActionDto;
import cn.xinbee.mdrs.util.JsonUtils;
import java.util.function.Function;

public class MailActionTransformer {

		public static Function<MailTopicMessage, Object> openMsg2DtoFun = topicMessage -> {
				final String mailActionStr = topicMessage.getMessage();
				return JsonUtils.deserialize(mailActionStr, MailOpenActionDto.class);
		};

		public static Function<Object, MailTopicMessage> openDto2MsgFun = dto -> {
				MailOpenActionDto openActionDto = (MailOpenActionDto) dto;
				return new MailTopicMessage(MessageTopic.MAIL_ACTION, JsonUtils.serialize(openActionDto));
		};

		public Object transform(MailTopicMessage topicMessage,
				Function<MailTopicMessage, Object> function) {
				return function.apply(topicMessage);
		}

		public MailTopicMessage transform(Object src, Function<Object, MailTopicMessage> func) {
				return func.apply(src);
		}


}
