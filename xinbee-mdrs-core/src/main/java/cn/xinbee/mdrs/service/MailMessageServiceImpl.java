package cn.xinbee.mdrs.service;

import cn.xinbee.mdrs.queue.MailActionQueueTemplate;
import java.util.Date;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

@Service
public class MailMessageServiceImpl implements MailMessageService {

    private MailActionQueueTemplate queueTemplate;

    public MailMessageServiceImpl(MailActionQueueTemplate queueTemplate) {
        this.queueTemplate = queueTemplate;
    }

    @Override
    public void receiveOpen(String base64TrackInf) {
        final MailOpenActionDto openAction = new MailOpenActionDto(DateTime.now(),
            base64TrackInf);
        queueTemplate.send(openAction);
    }

    public static class MailOpenActionDto{
        private DateTime actionDate;
        private String base64TrackInf;

        public MailOpenActionDto() {
        }

        public MailOpenActionDto(DateTime actionDate, String base64TrackInf) {
            this.actionDate = actionDate;
            this.base64TrackInf = base64TrackInf;
        }

        public DateTime getActionDate() {
            return actionDate;
        }

        public void setActionDate(DateTime actionDate) {
            this.actionDate = actionDate;
        }

        public String getBase64TrackInf() {
            return base64TrackInf;
        }

        public void setBase64TrackInf(String base64TrackInf) {
            this.base64TrackInf = base64TrackInf;
        }
    }
}
