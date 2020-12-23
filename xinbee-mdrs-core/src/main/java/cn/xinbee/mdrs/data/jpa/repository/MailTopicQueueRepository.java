package cn.xinbee.mdrs.data.jpa.repository;

import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage;
import cn.xinbee.mdrs.data.jpa.domain.MailTopicMessage.MessageTopic;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailTopicQueueRepository extends JpaRepository<MailTopicMessage, Long> {

		boolean existsByTopicAndLockedFalse(MessageTopic topic);

		MailTopicMessage findTopByTopicAndLockedFalseOrderByCreatedDateDesc(MessageTopic topic);

		List<MailTopicMessage> findAllByTopicAndLockedFalseOrderByCreatedDateDesc(MessageTopic topic,
				Pageable pageable);

		List<MailTopicMessage> findAllByTopicAndLockedTrue(MessageTopic topic);
}
