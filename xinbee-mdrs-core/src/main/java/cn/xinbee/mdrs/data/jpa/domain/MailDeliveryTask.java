package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntityAuditable;
import cn.xinbee.mdrs.service.dto.MailTaskSubmitDto;
import cn.xinbee.mdrs.util.JsonUtils;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.persistence.Version;
import org.joda.time.DateTime;

@Entity
public class MailDeliveryTask extends AbstractEntityAuditable<Long> {

    private String name;
    private Integer recipientCount;
    private TaskStatus status;
    private DateTime finishedDate;
    private String customFields;
    private Long version;

    public enum TaskStatus {
        INIT, SENDING, COMPLETE
    }

    public static MailDeliveryTask fromSubmitDto(MailTaskSubmitDto dto) {
        MailDeliveryTask task = new MailDeliveryTask();
        task.setName(dto.getName());
        task.setRecipientCount(dto.recipientCount());
        task.setStatus(TaskStatus.INIT);
        task.setCustomFields(JsonUtils.serialize(dto.getTemplateVars()));
        return task;
    }

    @Transient
    public Map<String, String> getTaskParams(){
        return JsonUtils.deserialize(customFields);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRecipientCount() {
        return recipientCount;
    }

    @Column(name = "recipient_count", nullable = false)
    public void setRecipientCount(Integer recipientCount) {
        this.recipientCount = recipientCount;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    @Column(name = "finished_date")
    public DateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(DateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    @Column(name = "custom_fields")
    public String getCustomFields() {
        return customFields;
    }

    public void setCustomFields(String customFields) {
        this.customFields = customFields;
    }

    @Version
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
