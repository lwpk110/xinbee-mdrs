package cn.xinbee.mdrs.data.jpa.repository;

import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryLog;
import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryLog.DeliveryStatus;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailDeliveryLogRepository extends JpaRepository<MailDeliveryLog, Long> {

    List<MailDeliveryLog> findAllByStatusInOrderByCreatedDateAsc(Collection<DeliveryStatus> statuses);

    List<MailDeliveryLog> findAllByStatus(DeliveryStatus status);
}
