package cn.xinbee.mdrs.service.dto;

public class MailOpenTrackInf {
    private String email;
    private String taskId;
    private String emailId;
    private String apiUser;

    public MailOpenTrackInf() {
    }

    public MailOpenTrackInf(String email, String taskId, String emailId, String apiUser) {
        this.email = email;
        this.taskId = taskId;
        this.emailId = emailId;
        this.apiUser = apiUser;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }
}
