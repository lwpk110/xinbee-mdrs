package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntityAuditable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.joda.time.DateTime;

@Entity
public class MailTopicMessage extends AbstractEntityAuditable<Long> {

		private MessageTopic topic;
		private String message;
		private Boolean locked = false;
		private DateTime pollDate;

		public enum MessageTopic {
				MAIL_ACTION
		}

		public MailTopicMessage() {
		}

		public MailTopicMessage(MessageTopic topic) {
				this.topic = topic;
		}

		public MailTopicMessage(MessageTopic topic, String message) {
				this.topic = topic;
				this.message = message;
		}

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		@Override
		public Long getId() {
				return super.getId();
		}

		@Enumerated(value = EnumType.STRING)
		public MessageTopic getTopic() {
				return topic;
		}

		public void setTopic(MessageTopic topic) {
				this.topic = topic;
		}

		@Lob
		@Basic(fetch = FetchType.LAZY)
		public String getMessage() {
				return message;
		}

		public void setMessage(String message) {
				this.message = message;
		}

		public Boolean getLocked() {
				return locked;
		}

		public void setLocked(Boolean locked) {
				this.locked = locked;
		}

		public void lockMessage() {
				this.setLocked(true);
		}

		public void recoveryLock() {
				this.setLocked(false);
		}

		@Column(name = "poll_date", nullable = false)
		public DateTime getPollDate() {
				return pollDate;
		}

		public void setPollDate(DateTime pollDate) {
				this.pollDate = pollDate;
		}
}
