package cn.xinbee.mdrs.data.jpa.repository;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTopicQueueRepository extends JpaRepository<MailTopicMessage, Long> {
    boolean existsByTopic(MailTopicMessage.MessageTopic topic);

    MailTopicMessage findTopByTopicOrderByCreatedDateDesc(MailTopicMessage.MessageTopic topic);
}
