package cn.xinbee.mdrs.service;

import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryTask;
import cn.xinbee.mdrs.service.dto.MailTaskSubmitDto;

public interface MailTaskService extends EntityService<MailDeliveryTask, Long> {

    MailDeliveryTask submit(MailTaskSubmitDto dto);

}
