package com.hd.message.dao;

import com.hd.message.entity.MessageSendRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息推送记录dao
 *
 * @author wang_yw
 */
@Repository
public interface MessageSendRecordRepository extends JpaRepository<MessageSendRecord, Integer> {
}
