package com.hd.message.dto;

import lombok.Data;


/**
 * 推送消息实体
 *
 * @author wang_yw
 */
@Data
public class MessageDTO {


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
     * 消息类型(系统消息/短信/邮件)
     */
    private MessageType messageType;

    /**
     * 消息发送类型（群发/单发）
     */
    private MessageSendType messageSendType;

    //--------------

    /**
     * 凭证
     */
    private String token;

}
