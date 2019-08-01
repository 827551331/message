package com.hd.message.dto;

import lombok.Data;

/**
 * http请求结果
 *
 * @author wang_yw
 */
@Data
public class OKHttpResponseDTO {

    private Integer httpCode;
    private String httpMessage;
    private Object httpResponse;

    public OKHttpResponseDTO(Integer httpCode, String httpMessage, Object httpResponse) {
        this.httpCode = httpCode;
        this.httpMessage = httpMessage;
        this.httpResponse = httpResponse;
    }
}
