package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.MailRecipient;
import cn.xinbee.mdrs.data.AbstractEntity;
import cn.xinbee.mdrs.util.JsonUtils;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import org.springframework.util.CollectionUtils;

@Entity
public class MailDeliveryRecipient extends AbstractEntity<Long> {

    private String recipients;
    private Integer count;

    public MailDeliveryRecipient() {
    }

    public MailDeliveryRecipient(long id, Collection<MailRecipient> recipients) {
        this.id = id;
        this.recipients = JsonUtils.serialize(recipients);
        this.count = CollectionUtils.isEmpty(recipients) ? 0 : recipients.size();
    }

    @Id
    @GeneratedValue
    @Override
    public Long getId() {
        return super.getId();
    }

    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
