package com.hd.message.entity;

import com.hd.message.dto.MessageSendStatus;
import com.hd.message.dto.MessageSendType;
import com.hd.message.dto.MessageType;
import lombok.Data;

import java.util.Date;


@Data
public class Message {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 业务系统中的ID
     */
    private String sourceId;

    /**
     * 目标人员
     */
    private String[] targetUsers;

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
     * 处理的接口
     */
    private String dealApi;

    /**
     * 消息创建时间
     */
    private Date messageCreateTime;

    /**
     * 消息类型(系统消息/短信/邮件)
     */
    private MessageType messageType;

    /**
     * 消息发送类型（群发/单发）
     */
    private MessageSendType messageSendType;

    /**
     * 发送人员
     */
    private String messageSendTarget;

    /**
     * 发送结果
     */
    private MessageSendStatus messageSendStatus;
}
