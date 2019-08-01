package com.hd.message.dto;

public enum MessageSendStatus {
    SUCCESS,        //发送成功
    FAIL,           //发送失败
    RETRYSUCCESS,   //重试发送成功
    RETRYFAIL       //重试发送失败
}
