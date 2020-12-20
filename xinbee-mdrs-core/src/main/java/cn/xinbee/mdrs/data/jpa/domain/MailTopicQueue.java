package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntityAuditable;
import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;

public class MailTopicQueue extends AbstractEntityAuditable<Long> {

    private String topic;
    private String message;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
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
}
