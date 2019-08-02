package com.hd.message.dao;

import com.hd.message.entity.MessageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 消息记录dao
 *
 * @author wang_yw
 */
@Repository
public interface MessageRecordRepository extends JpaRepository<MessageRecord, Integer> {
}
