package com.hd.message.entity;

import com.hd.message.dto.MessageType;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;


/**
 * 消息记录
 */
@Data
@Entity
@Table(name = "message_record")
public class MessageRecord {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务系统中的ID
     */
    private String sourceId;

    /**
     * 目标人员
     */
    private String targetUsers;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息体
     */
    private String content;

    /**
     * 消息URL
     */
    private String link;

    /**
     * 消息创建时间
     */
    private Date messageCreateTime;

    /**
     * 消息类型(系统消息/短信/邮件)
     */
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

}
