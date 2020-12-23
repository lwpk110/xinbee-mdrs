package cn.xinbee.mdrs.mail;

import cn.xinbee.mdrs.data.jpa.domain.MailAction;
import cn.xinbee.mdrs.data.jpa.domain.MailAction.ActionStatus;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.repository.MailActionRepository;
import cn.xinbee.mdrs.service.MailMessageServiceImpl.MailOpenActionDto;
import cn.xinbee.mdrs.service.dto.MailOpenTrackInf;
import cn.xinbee.mdrs.util.JsonUtils;
import java.nio.charset.StandardCharsets;
import org.joda.time.DateTime;
import org.springframework.util.Base64Utils;

public class DefaultMailActionMessageHandler implements MailActionMessageHandler{

		private final MailActionRepository actionRepository;

		private final MailActionTransformer mailActionTransformer = new MailActionTransformer();

	public 	DefaultMailActionMessageHandler(
				MailActionRepository actionRepository) {
				this.actionRepository = actionRepository;
		}

		@Override
		public Object handleMessage(MailTopicMessage msg) {
				final MailOpenActionDto openActionDto;
				try {
						openActionDto = convert(msg);
				} catch (Exception e) {
						throw new IllegalStateException(String.format("转换消息失败，msg:%s", msg), e);
				}
				handle(openActionDto);
				return new HandlerResult(true,msg.getId());
		}

		private MailOpenActionDto convert(MailTopicMessage topicMessage) {
				Object o = mailActionTransformer
						.transform(topicMessage, MailActionTransformer.openMsg2DtoFun);
				return (MailOpenActionDto) o;
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

		public static class HandlerResult {

				private boolean success;
				private Long msgId;

				public HandlerResult(boolean result, Long msgId) {
						this.success = result;
						this.msgId = msgId;
				}

				public boolean isSuccess() {
						return success;
				}

				public void setSuccess(boolean success) {
						this.success = success;
				}

				public Long getMsgId() {
						return msgId;
				}

				public void setMsgId(Long msgId) {
						this.msgId = msgId;
				}
		}
}
