package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntity;
import cn.xinbee.mdrs.util.JsonUtils;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import org.springframework.util.StringUtils;

@Entity
public class MailDeliveryChannel extends AbstractEntity<Integer> {

		private String sendFrom;
		private String reply;
		private String name;
		private String provider;
		private String host;
		private Integer port;
		private String user;
		private String pass;
		private String protocol;
		private String config;
		private Boolean deleted = false;
		private Boolean disabled = false;
		private ChannelProperties channelProperties;

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		public Integer getId() {
				return super.getId();
		}

		public String getName() {
				return name;
		}

		public String getConfig() {
				return config;
		}

		public void setConfig(String config) {
				this.config = config;
		}

		public void setName(String name) {
				this.name = name;
		}

		public String getProvider() {
				return provider;
		}

		public void setProvider(String provider) {
				this.provider = provider;
		}

		public String getHost() {
				return host;
		}

		public void setHost(String host) {
				this.host = host;
		}

		public Integer getPort() {
				return port;
		}

		public void setPort(Integer port) {
				this.port = port;
		}

		public String getUser() {
				return user;
		}

		public void setUser(String user) {
				this.user = user;
		}

		public String getPass() {
				return pass;
		}

		public void setPass(String pass) {
				this.pass = pass;
		}

		public String getProtocol() {
				return protocol;
		}

		public void setProtocol(String protocol) {
				this.protocol = protocol;
		}

		public String getSendFrom() {
				return sendFrom;
		}

		public void setSendFrom(String sendFrom) {
				this.sendFrom = sendFrom;
		}

		public String getReply() {
				return reply;
		}

		public void setReply(String reply) {
				this.reply = reply;
		}

		public Boolean getDeleted() {
				return deleted;
		}

		public void setDeleted(Boolean deleted) {
				this.deleted = deleted;
		}

		public Boolean getDisabled() {
				return disabled;
		}

		public void setDisabled(Boolean disabled) {
				this.disabled = disabled;
		}

		@Transient
		public ChannelProperties getChannelProperties() {
				if (StringUtils.hasText(this.config)) {
						return JsonUtils.deserialize(this.config, ChannelProperties.class);
				}
				return null;
		}

		public void setChannelProperties(
				ChannelProperties channelProperties) {
				this.channelProperties = channelProperties;
				this.config = JsonUtils.serialize(channelProperties);
		}

		public static class ChannelProperties {

				private Range<Integer> qps;

				public Range<Integer> getQps() {
						return qps;
				}

				public void setQps(Range<Integer> qps) {
						this.qps = qps;
				}
		}

}
