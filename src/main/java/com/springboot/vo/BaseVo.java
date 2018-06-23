package com.springboot.vo;

import com.springboot.enums.StatusEnum;

public class BaseVo {

    private Integer status = StatusEnum.SUCCESS_200.getCode();

    private Object data;

    private String msgCode;

    private String msgValue;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsgValue() {
        return msgValue;
    }

    public void setMsgValue(String msgValue) {
        this.msgValue = msgValue;
    }
}
