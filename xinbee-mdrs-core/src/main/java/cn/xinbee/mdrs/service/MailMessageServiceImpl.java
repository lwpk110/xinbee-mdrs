package cn.xinbee.mdrs.service;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage.MessageTopic;
import cn.xinbee.mdrs.mail.MailActionTransformer;
import cn.xinbee.mdrs.queue.MailActionQueueTemplate;
import cn.xinbee.mdrs.util.JsonUtils;
import java.util.Date;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
public class MailMessageServiceImpl implements MailMessageService {

		private MailActionQueueTemplate queueTemplate;
		private MailActionTransformer mailActionTransformer = new MailActionTransformer();

		public MailMessageServiceImpl(MailActionQueueTemplate queueTemplate) {
				this.queueTemplate = queueTemplate;
		}

		@Override
		public void receiveOpen(String base64TrackInf) {
				final MailOpenActionDto openAction = new MailOpenActionDto(DateTime.now(),
						base64TrackInf);
				queueTemplate.send(transform(openAction));
		}

		private MailTopicMessage transform(MailOpenActionDto openAction) {
				return mailActionTransformer.transform(openAction, MailActionTransformer.openDto2MsgFun);
		}

		public static class MailOpenActionDto {

				private DateTime actionDate;
				private String base64TrackInf;

				public MailOpenActionDto() {
				}

				public MailOpenActionDto(DateTime actionDate, String base64TrackInf) {
						this.actionDate = actionDate;
						this.base64TrackInf = base64TrackInf;
				}

				public DateTime getActionDate() {
						return actionDate;
				}

				public void setActionDate(DateTime actionDate) {
						this.actionDate = actionDate;
				}

				public String getBase64TrackInf() {
						return base64TrackInf;
				}

				public void setBase64TrackInf(String base64TrackInf) {
						this.base64TrackInf = base64TrackInf;
				}
		}
}
