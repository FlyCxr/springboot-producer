package com.springboot.enums;

/**
 * 用户操作错误枚举
 */
@SuppressWarnings("all")
public enum UserErrorEnum {

    USERNAME_NOT_EXIST("USER_ERROR_001","userName does not exist"),

    ACCOUNT_ALREADY_LOCK("USER_ERROR_002","account has been locked"),

    USERNAME_OR_PWD_NOT_RIGHT("USER_ERROR_003","userName or password is not correct");

    private String code;

    private String msg;

    UserErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
