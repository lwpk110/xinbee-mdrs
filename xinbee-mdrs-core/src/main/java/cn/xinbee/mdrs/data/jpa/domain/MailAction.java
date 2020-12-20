package cn.xinbee.mdrs.data.jpa.domain;

import cn.xinbee.mdrs.data.AbstractEntityAuditable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import org.joda.time.DateTime;

@Entity
@Table(name = "r_mail_actions")
public class MailAction extends AbstractEntityAuditable<Long> {

    private String email;
    private String taskId;
    private String mailingId;
    private String apiUser;
    private ActionStatus status;
    private DateTime actionDate;
    private String customVars;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Override
    public Long getId() {
        return super.getId();
    }

    public enum ActionStatus {
        SENT_SUCCESS, OPEN, CLICK
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMailingId() {
        return mailingId;
    }

    public void setMailingId(String mailingId) {
        this.mailingId = mailingId;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public ActionStatus getStatus() {
        return status;
    }

    public void setStatus(ActionStatus status) {
        this.status = status;
    }

    public DateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(DateTime actionDate) {
        this.actionDate = actionDate;
    }

    public String getCustomVars() {
        return customVars;
    }

    public void setCustomVars(String customVars) {
        this.customVars = customVars;
    }
}
