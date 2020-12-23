package cn.xinbee.mdrs.data.jpa.repository;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTopicQueueRepository extends JpaRepository<MailTopicMessage, Long> {
    boolean existsByTopicAndLockedFalse(MailTopicMessage.MessageTopic topic);

    List<MailTopicMessage> findAllByTopicAndLockedFalseOrderByCreatedDateDesc(MailTopicMessage.MessageTopic topic, Pageable pageable);
}
