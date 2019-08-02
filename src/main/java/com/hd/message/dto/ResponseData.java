package com.hd.message.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回对象
 *
 * @param <T>
 * @author wang_yw
 */
@Data
public class ResponseData<T> implements Serializable {

    public ResponseData(String rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
    }

    public ResponseData(String rtnCode, T rtnData) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
        this.rtnData = rtnData;
    }

    public ResponseData(String rtnCode, String rtnMsg, T rtnData) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
        this.rtnData = rtnData;
    }

    public static ResponseData getInstance(String rtnCode, String rtnMsg, Object rtnData) {
        return new ResponseData(rtnCode, rtnMsg, rtnData);
    }

    /**
     * 返回码
     */
    private String rtnCode;

    /**
     * 返回信息
     */
    private String rtnMsg;

    /**
     * 返回信息
     */
    private T rtnData;
}
