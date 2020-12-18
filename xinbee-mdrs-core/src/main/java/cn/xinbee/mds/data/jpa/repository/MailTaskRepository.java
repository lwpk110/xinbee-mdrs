package cn.xinbee.mdrs.data.jpa.repository;

import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTaskRepository extends JpaRepository<MailDeliveryTask, Long> {

}
