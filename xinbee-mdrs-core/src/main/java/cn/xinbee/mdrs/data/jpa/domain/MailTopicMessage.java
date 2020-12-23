package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntityAuditable;

import javax.persistence.*;

@Entity
public class MailTopicMessage extends AbstractEntityAuditable<Long> {

		private MessageTopic topic;
		private String message;
		private Boolean locked = false;

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

		public void lockMessage(){
				this.setLocked(true);
		}

		public void recoveryLock(){
				this.setLocked(false);
		}
}
