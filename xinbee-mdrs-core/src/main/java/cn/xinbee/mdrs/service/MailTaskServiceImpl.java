package cn.xinbee.mdrs.service;

import cn.xinbee.mdrs.MailRecipient;
import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryLog;
import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryRecipient;
import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryTask;
import cn.xinbee.mdrs.data.jpa.repository.MailDeliveryLogRepository;
import cn.xinbee.mdrs.data.jpa.repository.MailDeliveryRecipientRepository;
import cn.xinbee.mdrs.data.jpa.repository.MailTaskRepository;
import cn.xinbee.mdrs.service.dto.MailTaskSubmitDto;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MailTaskServiceImpl extends
    EntityServiceSupport<MailDeliveryTask, Long, MailTaskRepository> implements MailTaskService {

    private final MailDeliveryLogRepository logRepository;
    private final MailDeliveryRecipientRepository recipientRepository;

    @Autowired
    protected MailTaskServiceImpl(MailTaskRepository repository,
        MailDeliveryLogRepository logRepository,
        MailDeliveryRecipientRepository recipientRepository) {
        super(repository);
        this.logRepository = logRepository;
        this.recipientRepository = recipientRepository;
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MailDeliveryTask submit(MailTaskSubmitDto dto) {
        final Collection<MailRecipient> recipients = dto.getRecipients();
        MailDeliveryTask task = MailDeliveryTask.fromSubmitDto(dto);
        this.getRepository().save(task);
        MailDeliveryRecipient taskRecipient = new MailDeliveryRecipient(task.getId(),recipients);
        this.recipientRepository.save(taskRecipient);
        final Collection<MailDeliveryLog> logs = MailDeliveryLog
            .fromRecipients(task.getId(), recipients);
        this.logRepository.saveAll(logs);
        return task;
    }
}
