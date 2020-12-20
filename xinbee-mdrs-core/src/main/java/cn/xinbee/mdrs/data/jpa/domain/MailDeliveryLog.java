package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.MailRecipient;
import cn.xinbee.mdrs.data.AbstractEntityAuditable;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.joda.time.DateTime;

@Entity
public class MailDeliveryLog extends AbstractEntityAuditable<Long> {

    public static final int retryMax = 10;

    private Long taskId;
    private String name;
    private String email;
    private Integer channelId;
    private Integer retryCount = 0;
    private DeliveryStatus status;
    private DateTime invokeDate;
    private Integer templateId;
    private String subject;
    private Long version;

    public enum DeliveryStatus {
        INIT, SENDING, INTERRUPTED, OK, RETRYABLE, FAIL,
    }

    public static Collection<MailDeliveryLog> fromRecipients(
        long taskId,
        Collection<MailRecipient> recipients) {
        return recipients.stream().map(r -> {
            MailDeliveryLog log = new MailDeliveryLog();
            log.setTaskId(taskId);
            log.setEmail(r.getEmail());
            log.setName(r.getName());
            log.setStatus(DeliveryStatus.INIT);
            return log;
        }).collect(Collectors.toList());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return super.getId();
    }

    public Long getTaskId() {
        return taskId;
    }

    @Column(name = "retry_count")
    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    @Column(name = "task_id", nullable = false)
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "channel_id")
    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    @Column(name = "invoke_date")
    public DateTime getInvokeDate() {
        return invokeDate;
    }

    public void setInvokeDate(DateTime invokeDate) {
        this.invokeDate = invokeDate;
    }

    @Column(name = "template_id")
    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "MailDeliveryLog{" +
            "taskId=" + taskId +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", channelId=" + channelId +
            ", retryCount=" + retryCount +
            ", status=" + status +
            ", invokeDate=" + invokeDate +
            ", templateId=" + templateId +
            ", subject='" + subject + '\'' +
            ", version=" + version +
            ", id=" + id +
            "} " + super.toString();
    }
}
