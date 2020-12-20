package cn.xinbee.mdrs.service.dto;

import cn.xinbee.mdrs.MailRecipient;
import java.util.Collection;
import java.util.Map;
import org.springframework.util.CollectionUtils;

public class MailTaskSubmitDto {
    private Collection<MailRecipient> recipients;
    private String name;
    private Map<String,String> templateVars;

    public MailTaskSubmitDto(Collection<MailRecipient> recipients, String name,
        Map<String, String> templateVars) {
        this.recipients = recipients;
        this.name = name;
        this.templateVars = templateVars;
    }

    public int recipientCount(){
        return CollectionUtils.isEmpty(recipients) ? 0 : recipients.size();
    }

    public Collection<MailRecipient> getRecipients() {
        return recipients;
    }

    public void setRecipients(Collection<MailRecipient> recipients) {
        this.recipients = recipients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getTemplateVars() {
        return templateVars;
    }

    public void setTemplateVars(Map<String, String> templateVars) {
        this.templateVars = templateVars;
    }
}
