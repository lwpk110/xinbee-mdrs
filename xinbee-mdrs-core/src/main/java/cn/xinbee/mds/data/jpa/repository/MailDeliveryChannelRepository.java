package cn.xinbee.mdrs.data.jpa.repository;

import cn.xinbee.mdrs.data.jpa.domain.MailDeliveryChannel;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailDeliveryChannelRepository extends JpaRepository<MailDeliveryChannel, Integer> {

    Collection<MailDeliveryChannel> findAllByDisabledFalseAndDeletedFalse();
}
