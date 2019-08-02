package com.hd.message.entity;

import com.hd.message.dto.MessageSendStatus;
import com.hd.message.dto.NeedRetry;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 消息推送记录
 */
@Data
@Entity
@Table(name = "messagesend_record")
public class MessageSendRecord {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 消息ID
     */
    private Integer messageRecord_Id;


    /**
     * 发送目标人员
     */
    private String messageSendTarget;

    /**
     * 发送结果
     */
    @Enumerated(EnumType.STRING)
    private MessageSendStatus messageSendStatus;

    /**
     * 初始发送时间
     */
    private Date sendTime;

    /**
     * 发送失败原因
     */
    private String failReason;

    /**
     * 最后重试时间
     */
    private Date retrySendTime;

    /**
     * 重试次数
     */
    private Integer retryNum;

    /**
     * 是否需要重试
     */
    @Enumerated(EnumType.STRING)
    private NeedRetry needRetry;
}
